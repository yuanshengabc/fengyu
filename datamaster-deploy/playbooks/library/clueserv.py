#!/usr/bin/python

import os
import commands
import getopt
import sys
import time
import json

from ansible.module_utils.basic import *


def get_run_stat(path_ctrl, args):
    cmd = "python %s %s status" % (path_ctrl, args)
    run_ok,output = commands.getstatusoutput(cmd)
    serv_stat = ""
    if run_ok != 0:
        return run_ok,serv_stat

    jv = json.loads(output)
    return run_ok,jv["status"]


def ctrl_service(path_ctrl, args, code):
    cmd = "python %s %s %s" % (path_ctrl, args, code)
    run_ok,output = commands.getstatusoutput(cmd)
    return run_ok,output


def main():

    fields = {
            "name": {"required": True, "type": "str"},
            "path": {"required": False, "type": "str"},
            "state": {
                "default": "",
                "choices": ["started", "stopped", "restarted", "restarted_nice", "reloaded", "status"],
                "type": "str"
                },
            "arg": {"required": False, "type": "str", "default": ""},
            }

    module = AnsibleModule(argument_spec=fields)
    name = module.params["name"]
    path = module.params["path"]
    state = module.params["state"]
    args = module.params["arg"]

    if not path:
        path = "/opt/deepclue/" + name

    control_path = path + "/control/" + name + "_control.py"

    if state == "stopped" or state == "started":
        # get current service stat
        is_ok,serv_stat = get_run_stat(control_path, args)
        if is_ok != 0:
            module.fail_json(msg="get_run_stat(%s, %s) failed" % (control_path, args))

        # is the stat match?
        if state == serv_stat:
            module.exit_json(changed=False, msg="status [%s] of service is not changed." % state)

        # not match, do something
        if state == "stopped":
            is_ok,msg = ctrl_service(control_path, args, "stop")
        else:
            is_ok,msg = ctrl_service(control_path, args, "start")

        if is_ok != 0:
            module.fail_json(msg="ctrl_service(%s, %s) to %s failed" % (control_path, args, state))
        else:
            module.exit_json(changed=True, msg="change service status to %s." % state)

    elif state == "restarted" or state == "restarted_nice":
        if state == "restarted_nice":
            is_ok,serv_stat = get_run_stat(control_path, args)
            if is_ok != 0:
                module.fail_json(msg="get_run_stat(%s, %s) failed" % (control_path, args))

            if serv_stat == "stopped":
                module.exit_json(changed=False,
                    msg="service is stopped, don't start at stat [restart_nice]")

        # restart
        is_ok,msg = ctrl_service(control_path, args, "restart")

        if is_ok != 0:
            module.fail_json(msg="ctrl_service(%s) to %s failed" % (control_path, state))
        else:
            module.exit_json(changed=True, msg="restart service")

    elif state == "status":
        is_ok,msg = ctrl_service(control_path, args, "status")

        jv = json.loads(msg)

        if is_ok != 0:
            module.fail_json(msg="ctrl_service(%s) get status failed" % (control_path, state))
        else:
            module.exit_json(changed=False, status=jv["status"], pid=jv["pid"], msg=msg)


    module.fail_json(msg="state = %s is NOT supported yet." % state)


if __name__ == '__main__':
    main()
