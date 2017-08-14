#!/bin/bash
ip=$1
number=$2
./ioteststop.sh ${ip}

ssh magneto@${ip} "rm -rf /opt/datamaster/libs/"

scp -r ./build/libs/  magneto@${ip}:/opt/datamaster/

ssh magneto@${ip} "java -jar /opt/datamaster/libs/*.jar all --ioBean.recordNumber=${number} --ioBean.recordColumn=6&&
                   java -jar /opt/datamaster/libs/*.jar all --ioBean.recordNumber=${number} --ioBean.recordColumn=12&&
                   java -jar /opt/datamaster/libs/*.jar all --ioBean.recordNumber=${number} --ioBean.recordColumn=18"
#nohup ssh magneto@${ip} "java -jar /opt/datamaster/libs/*.jar all --ioBean.recordNumber=${number} --ioBean.recordColumn=6&&
#                         java -jar /opt/datamaster/libs/*.jar all --ioBean.recordNumber=${number} --ioBean.recordColumn=12&&
#                         java -jar /opt/datamaster/libs/*.jar all --ioBean.recordNumber=${number} --ioBean.recordColumn=18" &