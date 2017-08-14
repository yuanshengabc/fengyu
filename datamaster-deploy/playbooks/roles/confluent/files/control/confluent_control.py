#!/usr/bin/env python
#

import os
import commands
import subprocess
import shlex
import getopt
import sys
import time
import re


conf_name = "control_conf"


def get_pid_by_pidfile(pid_fname):
    if not os.path.isfile(pid_fname):
        return 0
    
    fpid = open(pid_fname)
    if not fpid:
        return 0
    pid = fpid.read()
    fpid.close()

    pid = pid.split("\n")
    if len(pid) == 0:
        return 0

    pid = int(pid[0])
    return pid

# return pid, 0 if not running
def is_running(pid_fname):
    pid = get_pid_by_pidfile(pid_fname)

    if pid != 0:
        cmd = "ps -p %d -f | grep es -q" % pid
        stat,output = commands.getstatusoutput(cmd)
        if stat == 0:
            return pid

    pattern = re.sub(r'([/]+)', '[/]+', pid_fname)
    cmd = "pgrep -l -f \"%s\"" % pattern
    stat,output = commands.getstatusoutput(cmd)
    if stat == 0:
        output = output.split('\n')
        for line in output:
            pair = line.split(' ')
            if pair[1] == "java":
                return int(pair[0])

    return 0

def is_pid_running(pid):
    if pid == 0:
        return False

    cmd = "ps -p %d" % pid
    stat,output = commands.getstatusoutput(cmd)
    if stat == 0:
        return True
    else:
        return False

def is_jps_running(pattern):
    cmd = "jps | grep '%s'" % pattern
    stat,output = commands.getstatusoutput(cmd)
    if stat == 0:
        output = output.split('\n')
        for line in output:
            pair = line.split(' ')
            if pair[1] == pattern:
                return int(pair[0])
    return 0

def run_start(path_cf_home, path_cf_logs):
    path_bin = path_cf_home + "/bin/"
    path_conf = path_cf_home + "/etc/"


    # 
    kafka_running = is_jps_running("SupportedKafka")

    if kafka_running:
        sys.stdout.write("kafka is already running. [pid:%d]\n" % kafka_running)
    else:
        cmd = "nohup %s/kafka-server-start %s/kafka/server.properties &" % \
                (path_bin, path_conf)
        p = subprocess.Popen(cmd,
                shell = True,
                stdin = open('/dev/null', 'r'),
                stdout = open('%s/kafka.std.log' % path_cf_logs, 'a+'),
                stderr = subprocess.STDOUT)
        p.wait()
        if p.returncode != 0:
            sys.stderr.write("start kafka with \"%s\" failed. return %d.\n" % 
                (cmd, p.returncode))
            return False

        time.sleep(5)

    #
    schema_reg_running = is_jps_running("SchemaRegistryMain")

    if schema_reg_running:
        sys.stdout.write("schema-registry is already running. [pid:%d]\n" % schema_reg_running)
    else:
        cmd = "nohup %s/schema-registry-start %s/schema-registry/schema-registry.properties &" % \
                (path_bin, path_conf)
        p = subprocess.Popen(cmd,
                shell = True,
                stdin = open('/dev/null', 'r'),
                stdout = open('%s/schema-registry.std.log' % path_cf_logs, 'a+'),
                stderr = subprocess.STDOUT)
        p.wait()
        if p.returncode != 0:
            sys.stderr.write("start schema-registry with \"%s\" failed. return %d.\n" % 
                (cmd, p.returncode))
            return False

    #
    return True


def stop_pid(pid, pname):
    cmd_stop = "kill %d" % pid
    stat,output = commands.getstatusoutput(cmd_stop)

    wait = 0
    while (wait < 20) and is_pid_running(pid):
        time.sleep(1)
        wait += 1

    if is_pid_running(pid):
        cmd_stop = "kill -9 %d" % pid
        stat,output = commands.getstatusoutput(cmd_stop)
        sys.stderr.write("stop %s by kill -9\n" % pname)

        wait = 0
        while (wait < 10) and is_pid_running(pid):
            stat,output = commands.getstatusoutput(cmd_stop)
            time.sleep(1)
            wait += 1
        
    return True


def run_stop():

    #
    kafka_running = is_jps_running("SupportedKafka")
    if not kafka_running:
        sys.stdout.write("kafka is not running.\n")
    else:
        stop_pid(kafka_running, "kafka")

    #
    schema_reg_running = is_jps_running("SchemaRegistryMain")
    if not schema_reg_running:
        sys.stdout.write("schema-registry is not running.\n")
    else:
        stop_pid(schema_reg_running, "schema_reg")

    #
    return True

def run_status():

    kafka_running = is_jps_running("SupportedKafka")

    schema_reg_running = is_jps_running("SchemaRegistryMain")

    if kafka_running == 0 and schema_reg_running == 0:
        status = "stopped"
    elif kafka_running and schema_reg_running:
        status = "started"
    else:
        status = "sick"
    
    return {"status": status, "kafka_pid": kafka_running, "schema_reg_pid": schema_reg_running}


def usage():
    sys.stderr.write("Usage: confluent_control.py {start|stop|restart|reload|status}\n")

def main():

    # args
    argc = len(sys.argv)
    if argc != 2:
        usage()
        exit(1)

    command = sys.argv[1].lower()

    confluent_home = sys.path[0] + "/../"
    confluent_path_log = ""

    # config
    conf_fname = sys.path[0] + "/" + conf_name + ".py"
    if os.path.isfile(conf_fname):
        conf = __import__(conf_name)
        if conf.confluent_home:
            confluent_home = conf.confluent_home
        if conf.confluent_path_log:
            confluent_path_log = conf.confluent_path_log

    if not confluent_path_log:
        pid_file_name = confluent_home + "/logs"

    is_ok = False
    # run command
    if command == "start":
        is_ok = run_start(confluent_home, confluent_path_log)
    elif command == "stop":
        is_ok = run_stop()
    elif command == "restart":
        is_ok = run_stop()
        if not is_ok:
            exit(1)
        is_ok = run_start(confluent_home, confluent_path_log)
    elif command == "reload":
        pass
    elif command == "status":
        stat = run_status()
        sys.stdout.write('{"status": "%s", "pid": 0, "kafka_pid": %d, "schema_reg_pid": %d}\n' % \
                        (stat["status"], stat["kafka_pid"], stat["schema_reg_pid"]))
        is_ok = True
    else:
        sys.stderr.write("unknown parameter %s\n", command)
        usage()
        exit(1)

    if is_ok:
        exit(0)
    else:
        exit(1)


if __name__ == "__main__":
    main()
