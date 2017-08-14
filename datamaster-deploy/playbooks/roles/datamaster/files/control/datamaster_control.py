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


conf_name = "datamaster_conf"



# return pid, 0 if not running
def is_running(datamaster_pattern):

    #pattern = re.sub(r'([/]+)', '[/]+', pid_fname)
    cmd = "jps |grep %s" % datamaster_pattern
    stat,output = commands.getstatusoutput(cmd)
    if stat == 0:
        output = output.split('\n')
        for line in output:
            pair = line.split(' ')
            return int(pair[0])

    return 0

def run_start(datamaster_pattern,datamaster_strat_code,path_logs):

    #if os.path.exists(path_logs)== False:
    #    os.mkdir(path_logs)
    datamaster_running = is_running(datamaster_pattern)

    if datamaster_running:
        sys.stdout.write("datamasterserver is already running. [pid:%d]\n" %datamaster_running)
        return False
    else:
        cmd = "nohup "+datamaster_strat_code+"&"
        #stat,output = commands.getstatusoutput(cmd)
        p = subprocess.Popen(cmd,
                             shell=True,
                             stdin=open('/dev/null', 'r'),
                             stdout=open(path_logs, 'a+'),
                             stderr=subprocess.STDOUT)
        p.wait()
        if p.returncode == 0:
            #sys.stderr.write(output
            sys.stderr.write("datamasterserver start success\n")
            return True
        else:
            sys.stderr.write("start datamasterserver with \"%s\" failed. return %d.\n" %
                             (cmd, p.returncode))
        return False


def run_stop(datamaster_pattern):
    #
    #
    datamaster_running = is_running(datamaster_pattern)

    if not datamaster_running:
        sys.stdout.write("datamastersercer is not running.\n")
        return False
    else:
        cmd = "kill -9 %d"% datamaster_running
        stat,output = commands.getstatusoutput(cmd)
        if stat == 0:
            sys.stderr.write(output)
            sys.stderr.write("datamasterserver shutdown success\n")
            return True
        return False


def run_status(datamaster_pattern):
    datamaster_running = is_running(datamaster_pattern)

    if datamaster_running == 0:
        status = "stopped"
    elif datamaster_running:
        status = "started"
    else:
        status = "sick"

    return {"status": status, "pid": datamaster_running}


def usage():
    sys.stderr.write("Usage: datamaster_control.py {start|stop|restart|reload|status}\n")

def main():

    # args
    argc = len(sys.argv)
    if argc != 2:
        usage()
        exit(1)

    command = sys.argv[1].lower()

    # config
    conf_fname = sys.path[0] + "/" + conf_name+'.py'
    if os.path.isfile(conf_fname):
        conf = __import__(conf_name)
        if conf.datamaster_pattern:
            datamaster_pattern = conf.datamaster_pattern
        else:
            datamaster_pattern = "cleaner"
        if conf.datamaster_start_code:
            datamaster_start_code=conf.datamaster_start_code
        else:
            sys.stderr.write("unfind  datamaster_start_code", conf_fname)
            exit(1)
        if conf.path_logs:
            path_logs = conf.path_logs
        else:
            path_logs = "/opt/magneto/log"
    else:
        sys.stderr.write("unfind  %s\n", conf_fname)
        exit(1)

    is_ok = False
    # run command
    if command == "start":
        is_ok = run_start(datamaster_pattern,datamaster_start_code,path_logs)
    elif command == "stop":
        is_ok = run_stop(datamaster_pattern)
    elif command == "restart":
        is_ok = run_stop(datamaster_pattern)
        if not is_ok:
            exit(1)
        is_ok = run_start(datamaster_pattern, datamaster_start_code,path_logs)
    elif command == "reload":
        pass
    elif command == "status":
        stat = run_status(datamaster_pattern)
        sys.stdout.write('{"status": "%s", "pid": %d}\n' % \
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
