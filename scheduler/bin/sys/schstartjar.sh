#!/bin/bash
lc=`ps -ef | grep scheduler- | wc -l`
name=$1
if  [ $lc -gt 1 ];then
    echo "scheduler has been started"
else
    nohup java -jar ${SCHPATH}/genlibs/scheduler-*.jar > /data/scheduler/log/scheduler-start.log &
    echo now starting,the log is in /data/scheduler/log/scheduler-start.log.
fi
