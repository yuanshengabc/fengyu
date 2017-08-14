#!/bin/bash

nohup /opt/dmcomponents/confluent/bin/zookeeper-server-start /opt/dmcomponents/confluent/etc/kafka/zookeeper.properties > /data/datamaster/confluent/logs/zookeeper.log &
echo "/data/datamaster/confluent/logs/zookeeper.log"

sleep 10

nohup /opt/dmcomponents/confluent/bin/kafka-server-start /opt/dmcomponents/confluent/etc/kafka/server.properties > /data/datamaster/confluent/logs/kafka.log &
echo "/data/datamaster/confluent/logs/kafka.log"

sleep 10

nohup /opt/dmcomponents/confluent/bin/schema-registry-start /opt/dmcomponents/confluent/etc/schema-registry/schema-registry.properties > /data/datamaster/confluent/logs/schema-registry.log &
echo "/data/datamaster/confluent/logs/schema-registry.log"
