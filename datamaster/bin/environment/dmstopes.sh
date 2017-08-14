#!/bin/bash

isESExist=`jps -l|grep Elasticsearch|wc -l`
if [ $isESExist -gt 0 ]; then
    echo "Elasticsearch stopped."
    espid=`cat /data/datamaster/elasticsearch/elasticsearch.pid`
    kill $espid
else 
    echo "no running elasticsearch process."
    if [ -f "/data/datamaster/elasticsearch/elasticsearch.pid" ]; then 
        echo "remove elasticsearch.pid."
        rm -f "/data/datamaster/elasticsearch/elasticsearch.pid"
    fi
fi




