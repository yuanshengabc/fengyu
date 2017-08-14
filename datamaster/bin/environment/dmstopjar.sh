#!/bin/bash
ps -ef | grep cleaner-0.1.0-SNAPSHOT.jar | awk '{print $2}'|while read pid
do
	kill -9 $pid > /dev/null 2>&1
done
