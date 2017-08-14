#!/bin/bash

jps -l | grep -q Elasticsearch

if (( $? == 0 )); then
    echo "ElasticSearch has already been started. Exit."
    exit
fi

system_memory_in_mb=`free -m | awk '/:/ {print $2;exit}'`

half_system_memory_in_mb=`expr $system_memory_in_mb / 2048`
if [ $half_system_memory_in_mb -ge 30 ];then
	half_system_memory_in_mb=28
fi

if test $# -ne 0; then
    ES_HEAP_SIZE=$1 /opt/dmcomponents/elasticsearch/bin/elasticsearch -d -p /data/datamaster/elasticsearch/elasticsearch.pid -Djna.nosys=true
else
    ES_HEAP_SIZE=${half_system_memory_in_mb}g /opt/dmcomponents/elasticsearch/bin/elasticsearch -d -p /data/datamaster/elasticsearch/elasticsearch.pid -Djna.nosys=true
fi
