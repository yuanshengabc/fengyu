#!/usr/bin/env python
#

import os
import commands
import getopt
import sys
import time
import re


conf_name = "control_conf"
cassandra_home = ""
pid_file_name = ""
cassandra_bin = ""


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
        cmd = "ps -p %d -f | grep cassandra -q" % pid
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


def run_start(bin_path, pid_fname):
    running = is_running(pid_fname)
    if running:
        sys.stdout.write("cassandra is already running. [pid:%d]\n" % running)
        return True

    cmd_start = "%s/cassandra -p %s </dev/null &>/dev/null" % (bin_path, pid_fname)
    print cmd_start
    stat,output = commands.getstatusoutput(cmd_start)

    if stat != 0:
        sys.stderr.write("start cassandra with \"%s\" failed. return %d, msg=%s\n" % 
            (cmd_start, stat, output))
        return False

    return True

def run_stop(bin_path, pid_fname):
    pid = is_running(pid_fname)
    if not pid:
        sys.stdout.write("cassandra is not running.")
        return True

    cmd_drain = "%s/nodetool drain" % bin_path
    stat,output = commands.getstatusoutput(cmd_drain)

    cmd_stop = "kill %d" % pid
    stat,output = commands.getstatusoutput(cmd_stop)

    wait = 0
    while (wait < 10) and is_pid_running(pid):
        time.sleep(1)
        wait += 1

    if is_pid_running(pid):
        cmd_stop = "kill -9 %d" % pid
        stat,output = commands.getstatusoutput(cmd_stop)
        sys.stderr.write("stop cassandra by kill -9")
        os.remove(pid_fname)

        wait = 0
        while (wait < 10) and is_pid_running(pid):
            stat,output = commands.getstatusoutput(cmd_stop)
            time.sleep(1)
            wait += 1
        
    return True


def run_status(pid_fname):
    running = is_running(pid_fname)
    if running:
        status = "started"
    else:
        status = "stopped"
    
    return {"status": status, "pid": running}


def usage():
    sys.stderr.write("Usage: cassandra_control.py {start|stop|restart|reload|status}\n")

def main():

    # args
    argc = len(sys.argv)
    if argc != 2:
        usage()
        exit(1)

    command = sys.argv[1].lower()

    # config
    conf_fname = sys.path[0] + "/" + conf_name + ".py"
    if os.path.isfile(conf_fname):
        conf = __import__(conf_name)
        if conf.cassandra_home:
            cassandra_home = conf.cassandra_home
        if conf.pid_file_name:
            pid_file_name = conf.pid_file_name

    if not cassandra_home:
        cassandra_home = sys.path[0] + "/../"

    cassandra_bin = cassandra_home + "/bin/"

    if not pid_file_name:
        pid_file_name = cassandra_home + "/run/cassandra.pid"


    is_ok = False
    # run command
    if command == "start":
        is_ok = run_start(cassandra_bin, pid_file_name)
    elif command == "stop":
        is_ok = run_stop(cassandra_bin, pid_file_name)
    elif command == "restart":
        is_ok = run_stop(cassandra_bin, pid_file_name)
        if not is_ok:
            exit(1)
        is_ok = run_start(cassandra_bin, pid_file_name)
    elif command == "reload":
        pass
    elif command == "status":
        stat = run_status(pid_file_name)
        sys.stdout.write('{"status": "%s", "pid": %d}\n' % (stat["status"], stat["pid"]))
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
