#!/bin/bash

master_ip=`grep discovery.zen.ping.unicast.hosts < /opt/dmcomponents/elasticsearch/config/elasticsearch.yml | grep -oP "(\d+\.){3}\d+"`

command=$1

commandlist=$'cathealth : 查看集群状态
cataliases : 查看联名信息
catallocation : 查看分布信息
catindices : 查看索引信息
catcount : 查看文档数量
catfielddata : 查看fielddata信息
catmaster : 查看master节点
catnodes : 查看node信息
catpendingtasks : 查看挂起的任务
catplugins : 查看插件信息
catrecovery : 查看recovery信息
catthreadpool : 查看线程池信息
**catshards : 查看分片信息
**clusterhealth : 查看集群健康状况
**clusterstate : 查看集群状态
clusterstats : 查看集群统计信息
clusterpendingtasks : 查看集群挂起的任务
nodesstats : 查看集群节点统计信息
nodesinfo : 查看集群节点信息'

case "$command" in
    "-l")
        echo  "${commandlist}" ;;
    "")
        echo please input the command as follow
        echo  "${commandlist}" ;;
    "cathealth")
        curl -XGET ${master_ip}:9200/_cat/health?v ;;
    "cataliases")
        curl ${master_ip}:9200/_cat/aliases?v ;;
    "catallocation")
        curl ${master_ip}:9200/_cat/allocation?v ;;
    "catindices")
        curl ${master_ip}:9200/_cat/indices?v ;;
    "catcount")
        echo "Please input index name : (mgobject mgattachment)"
        read name
        curl ${master_ip}:9200/_cat/count/${name}?v ;;
    "catfielddata")
        curl ${master_ip}:9200/_cat/fielddata?v ;;
    "catmaster")
        curl ${master_ip}:9200/_cat/master?v ;;
    "catnodes")
        curl -s ${master_ip}:9200/_cat/nodes?v ;;
    "catpendingtask")
        curl ${master_ip}:9200/_cat/pendingi_tasks?v ;;
    "catplugins")
        curl ${master_ip}:9200/_cat/plugins?v ;;
    "catrecovery")
        curl ${master_ip}:9200/_cat/recovery?v ;;
    "catthreadpool")
        curl ${master_ip}:9200/_cat/thread_pool?v ;;
    "catshards")
        curl ${master_ip}:9200/_cat/shards?v ;;
    "clusterhealth")
        curl ${master_ip}:9200/_cluster/health?pretty=true ;;
    "clusterstate")
        curl ${master_ip}:9200/_cluster/state?pretty=true ;;
    "clusterstats")
        curl ${master_ip}:9200/_cluster/stats?pretty=true ;;
    "clusterpendingtasks")
        curl ${master_ip}:9200/_cluster/pending_tasks?pretty=true ;;
    "nodesstats")
        curl ${master_ip}:9200/_nodes/stats?pretty=true ;;
    "nodesinfo")
        curl ${master_ip}:9200/_nodes?pretty=true ;;
    *)
        echo "invalid command" ;;
esac
