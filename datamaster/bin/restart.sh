#!/bin/bash
cd /opt/datamaster/
git pull
./gradlew clean build -Denv=$1
./bin/datamaster stop
sleep 1s
./bin/datamaster start $1
echo "start log is in /data/log/buildjar.log"
