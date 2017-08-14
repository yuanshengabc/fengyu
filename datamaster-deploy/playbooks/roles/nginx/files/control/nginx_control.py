#!/usr/bin/env python
#

import os
import commands
import getopt
import sys
import time
import re
import json

conf_name = "nginx_conf"
mesos_home = ""


def run_start(m_mode):
    status = run_status(m_mode)
    if status['status'] is 'started':
        sys.stdout.write("nginx-%s is running.\n" % m_mode)
        return True

    cmd_stop = "service %s start" % m_mode
    stat,output = commands.getstatusoutput(cmd_stop)
    if stat is 0:
        return True
    else:
        return False

def run_stop(m_mode):
    status = run_status(m_mode)
    if status['status'] is 'stoped':
        sys.stdout.write("nginx-%s is not running.\n" % m_mode)
        return True

    cmd_stop = "service %s stop" % m_mode
    stat,output = commands.getstatusoutput(cmd_stop)
    if stat is 0:
        return True
    else:
        return False


def run_status(m_mode):
    '''ret = {'status':'?','pid':-1,'msg':''}
    cmd = "service %s status"% m_mode
    out = os.popen(cmd)
    ret['msg']= out.read()
    output = ret['msg'].split('\n')
    for line in output:
        if 'Active' in line:
            if 'running' in line:
                ret['status']='started'
            else:
                ret['status']='stopped'
        elif 'Main PID' in line:
            try:
                mpid_str= line.strip()
                mpid    = mpid_str.split(':')[1].split('(')[0]
                ret['pid']=int(mpid)
            except Exception, e:
                pass
    return ret'''
    ret = {'status':'stopped','pid':-1}
    cmd = "service nginx status"
    stat,output = commands.getstatusoutput(cmd)
    if output!='':
        for line in output.split('\n'):
            if "Active" in line:
                if "running" in line:
                    ret['status']= 'started'
                else:
                    break
            if 'Main PID' in line:
                pid_str = line.split(':')[1].split('(')[0].strip()
                ret['pid'] = int(pid_str)
    return ret


def usage():
    sys.stderr.write("Usage: mesos_control.py <-m {master|agent} > {start|stop|restart|reload|status}\n")


def main():

    service_name = "nginx"
    nginx_home = ""
    if len(sys.argv) != 2:
        usage()
        exit(1)
    else:
        command = sys.argv[1].lower()

    if not command:
        sys.stderr.write("command NOT set.\n")
        usage()
        exit(1)

    # config
    conf_fname = sys.path[0] + "/" + conf_name + ".py"
    if os.path.isfile(conf_fname):
        conf = __import__(conf_name)
        if conf.nginx_home:
            nginx_home = conf.nginx_home
        if conf.service_name:
            service_name= conf.service_name

    if not nginx_home:
        nginx_home = sys.path[0] + "/../"



    is_ok = False
    # run command
    if command == "start":
        is_ok = run_start(service_name)
    elif command == "stop":
        is_ok = run_stop(service_name)
    elif command == "restart":
        is_ok = run_stop(service_name)
        if not is_ok:
            exit(1)
        is_ok = run_start(service_name)
    elif command == "reload":
        pass
    elif command == "status":
        stat = run_status(service_name)
        sys.stdout.write(json.dumps(stat)+'\n')
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
