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
    zk_running = is_jps_running("QuorumPeerMain")

    if zk_running:
        sys.stdout.write("zookeeper is already running. [pid:%d]\n" % zk_running)
    else:
        cmd = "nohup sh %s/zkServer.sh start &" % \
                (path_bin)
        p = subprocess.Popen(cmd,
                shell = True,
                stdin = open('/dev/null', 'r'),
                stdout = open('%s/zookeeper.std.log' % path_cf_logs, 'a+'),
                stderr = subprocess.STDOUT)
        p.wait()
        if p.returncode != 0:
            sys.stderr.write("start zookeeper with \"%s\" failed. return %d.\n" % 
                (cmd, p.returncode))
            return False

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


def run_stop(path_bin):
    #
    zk_running = is_jps_running("QuorumPeerMain")
    if not zk_running:
        sys.stdout.write("zookeeper is not running.\n")
    else:
        cmd_stop = "sh %s/zkServer.sh stop"% path_bin
        stat,output = commands.getstatusoutput(cmd_stop)
        #stop_pid(zk_running, "zookeeper")
    return True

def run_status():
    zk_running = is_jps_running("QuorumPeerMain")

    if zk_running == 0 :
        status = "stopped"
    else:
        status = "started"
    
    return {"status": status, "pid": zk_running}


def usage():
    sys.stderr.write("Usage: confluent_control.py {start|stop|restart|reload|status}\n")

def main():

    # args
    argc = len(sys.argv)
    if argc != 2:
        usage()
        exit(1)

    command = sys.argv[1].lower()

    zookeeper_home = sys.path[0] + "/../"
    zookeeper_path_log = ""

    # config
    conf_fname = sys.path[0] + "/" + conf_name + ".py"
    if os.path.isfile(conf_fname):
        conf = __import__(conf_name)
        if conf.zookeeper_home:
            zookeeper_home = conf.zookeeper_home
        if conf.zookeeper_path_log:
            zookeeper_path_log = conf.zookeeper_path_log

    if not zookeeper_path_log:
        pid_file_name = zookeeper_home + "/logs"

    is_ok = False
    # run command
    if command == "start":
        is_ok = run_start(zookeeper_home, zookeeper_path_log)
    elif command == "stop":
        is_ok = run_stop(zookeeper_home+'/bin')
    elif command == "restart":
        is_ok = run_stop(zookeeper_home+'/bin')
        if not is_ok:
            exit(1)
        is_ok = run_start(zookeeper_home, zookeeper_path_log)
    elif command == "reload":
        pass
    elif command == "status":
        stat = run_status()
        sys.stdout.write('{"status": "%s", "pid": %d }\n' % \
                        (stat["status"], stat["pid"]))
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
