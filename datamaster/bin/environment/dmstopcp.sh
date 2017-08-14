#!/bin/bash

srpid=`jps -l | grep SchemaRegistryMain | awk '{print $1}'`
if [ ! $srpid == "" ]; then
    echo "Stop SchemaRegistry"
    kill $srpid
else 
    echo "no SchemaRegistry found"
fi

kfpid=`jps -l | grep SupportedKafka | awk '{print $1}'`
if [ ! $kfpid == "" ]; then
    echo "Stop Kafka"
    kill $kfpid
    sleep 10
else 
    echo "no Kafka found"
fi



zkpid=`jps -l | grep QuorumPeerMain | awk '{print $1}'`
if [ ! $zkpid == "" ]; then
    echo "Stop Zookeeper"
    kill $zkpid
else 
    echo "no Zookeeper found"
fi
