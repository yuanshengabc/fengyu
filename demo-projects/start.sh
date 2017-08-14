#!/usr/bin/env bash



echo "stop app-a"
ps -ef | grep java | grep "app-a-0.0.1-SNAPSHOT.jar" | awk '{print $2}' | xargs kill -9
echo "start app-a"
nohup java -jar output/app-a-0.0.1-SNAPSHOT.jar &
echo "start app-a success"

echo "stop app-b"
ps -ef | grep java | grep "app-b-0.0.1-SNAPSHOT.jar" | awk '{print $2}' | xargs kill -9
echo "start app-b"
nohup java -jar output/app-b-0.0.1-SNAPSHOT.jar &
echo "start app-b success"


echo "stop app-c"
ps -ef | grep java | grep "app-c-0.0.1-SNAPSHOT.jar" | awk '{print $2}' | xargs kill -9
echo "start app-c"
nohup java -jar output/app-c-0.0.1-SNAPSHOT.jar &
echo "start app-c success"

echo "ok"