#!/usr/bin/env python
#

import os
import commands
import getopt
import sys
import time
import re


conf_name = "control_conf"
mesos_home = ""


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
def is_running(m_mode):

    #pattern = re.sub(r'([/]+)', '[/]+', pid_fname)
    pattern = 'mesos-%s' % m_mode
    cmd = "pgrep -l \"%s\"" % pattern
    stat,output = commands.getstatusoutput(cmd)
    if stat == 0:
        output = output.split('\n')
        for line in output:
            pair = line.split(' ')
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


def run_start(m_home, m_mode):
    running = is_running(m_mode)
    if running:
        sys.stdout.write("mesos-%s is already running. [pid:%d]\n" % (m_mode, running))
        return True

    proc = "mesos-" + m_mode

    cmd_start = "%s/sbin/mesos-daemon.sh %s" % (m_home, proc)
    stat,output = commands.getstatusoutput(cmd_start)

    if stat != 0:
        sys.stderr.write("start mesos-%s with \"%s\" failed. return %d, msg=%s\n" % 
            (m_mode, cmd_start, stat, output))
        return False

    return True

def run_stop(m_home, m_mode):
    pid = is_running(m_mode)
    if not pid:
        sys.stdout.write("mesos-%s is not running.\n" % m_mode)
        return True

    cmd_stop = "kill %d" % pid
    stat,output = commands.getstatusoutput(cmd_stop)

    wait = 0
    while (wait < 10) and is_pid_running(pid):
        time.sleep(1)
        wait += 1

    if is_pid_running(pid):
        cmd_stop = "kill -9 %d" % pid
        stat,output = commands.getstatusoutput(cmd_stop)
        sys.stderr.write("stop mesos-%s by kill -9\n" % m_mode)

        wait = 0
        while (wait < 10) and is_pid_running(pid):
            stat,output = commands.getstatusoutput(cmd_stop)
            time.sleep(1)
            wait += 1
        
    return True


def run_status(m_mode):
    running = is_running(m_mode)
    if running:
        status = "started"
    else:
        status = "stopped"
    
    return {"status": status, "pid": running}


def usage():
    sys.stderr.write("Usage: mesos_control.py <-m {master|agent} > {start|stop|restart|reload|status}\n")


def main():

    mode = ""
    opts, args = getopt.getopt(sys.argv[1:], "hm:", "")

    for name, value in opts:
        if name == "-h":
            usage()
            exit(0)
        elif name == "-m":
            mode = value.strip()
        else:
            sys.stderr.write("unknow argument %s\n" % name)
            usage()
            exit(1)

    if not mode:
        sys.stderr.write("mode NOT set.\n")
        usage()
        exit(1)

    if len(args) != 1:
        usage()
        exit(1)
    else:
        command = args[0].lower()

    if not command:
        sys.stderr.write("command NOT set.\n")
        usage()
        exit(1)

    # config
    conf_fname = sys.path[0] + "/" + conf_name + ".py"
    if os.path.isfile(conf_fname):
        conf = __import__(conf_name)
        if conf.mesos_home:
            mesos_home = conf.mesos_home

    if not mesos_home:
        mesos_home = sys.path[0] + "/../"

    is_ok = False
    # run command
    if command == "start":
        is_ok = run_start(mesos_home, mode)
    elif command == "stop":
        is_ok = run_stop(mesos_home, mode)
    elif command == "restart":
        is_ok = run_stop(mesos_home, mode)
        if not is_ok:
            exit(1)
        is_ok = run_start(mesos_home, mode)
    elif command == "reload":
        pass
    elif command == "status":
        stat = run_status(mode)
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
