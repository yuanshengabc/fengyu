#!/bin/bash
ps -ef | grep scheduler- | awk '{print $2}'|while read pid
do
	kill -9 $pid > /dev/null 2>&1
done
