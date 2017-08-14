#!/bin/bash
lc=`ps -ef | grep cleaner- | wc -l`
name=$1
if  [ $lc -gt 1 ];then
    echo "datamaster has been started"
else
    nohup java -jar ${DMPATH}/genlibs/cleaner-*.jar --spring.profiles.active=$1 > /data/log/datamaster/startjar.log &
    echo now starting $1,the log is in /data/log/datamaster/startjar.log.
fi
