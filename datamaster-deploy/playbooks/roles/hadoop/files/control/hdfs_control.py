#!/usr/bin/env python
#

import os
import commands
import getopt
import sys
import time
import re
import json


conf_name = "hdfs_conf"
es_home = ""
pid_file_name = ""
es_bin = ""
es_env = ""


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

def is_running(pid_fname):
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


def run_start(path,hdfs_cluster_name):
    
    cmd_stop = "%s/start-all.sh -format %s"%(path,hdfs_cluster_name)
    stat,output = commands.getstatusoutput(cmd_stop)
    if not stat:
        status = run_status()['status']
        if status == "started":
            sys.stdout.write("start success!\n")
            return True
    sys.stdout.write("start failed\n")
    return False



def run_stop(path):

    cmd_stop = "%s/stop-all.sh"% path
    stat,output = commands.getstatusoutput(cmd_stop)
    if not stat:
        if run_status()['status'] == "stopped":
            sys.stdout.write("stop success!\n")
            return True
    sys.stdout.write("stop failed\n")
    return False


def run_status():
    nn_running = is_jps_running("NameNode")
    rm_running = is_jps_running("ResourceManager")
    dn_running = is_jps_running("DataNode")
    nm_running = is_jps_running("NodeManager")
    if nn_running or rm_running or dn_running or nm_running:
        status = "started"
    else:
        status = "stopped"
    
    return {"status": status,"pid":0, "namenode_pid":
            nn_running,"resourcemanager_pid":rm_running,"datanode_pid":dn_running,"nodemanager":nm_running}


def usage():
    sys.stderr.write("Usage: hdfs_control.py {start|stop|restart|reload|status}\n")

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
        if conf.path_sbin:
            path_sbin = conf.path_sbin
        if conf.hdfs_cluster_name:
            hdfs_cluster_name = conf.hdfs_cluster_name
    if not path_sbin:
        path_sbin= "/opt/hadoop/sbin"
    if not hdfs_cluster_name:
        hdfs_cluster_name = 'test'



    is_ok = False
    # run command
    if command == "start":
        is_ok = run_start(path_sbin,hdfs_cluster_name)
    elif command == "stop":
        is_ok = run_stop(path_sbin)
    elif command == "restart":
        is_ok = run_stop(path_sbin)
        if not is_ok:
            exit(1)
        is_ok = run_start(path_sbin)
    elif command == "reload":
        pass
    elif command == "status":
        stat = run_status()
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
