#!/bin/bash
list=`/opt/dmcomponents/confluent/bin/kafka-topics --list --zookeeper localhost:2181`
for x in $list
do
	if [ $x = __consumer_offsets -o $x = _schemas ];then
		:
	else
		/opt/dmcomponents/confluent/bin/kafka-topics --delete --zookeeper localhost:2181 --topic $x
	fi
done
