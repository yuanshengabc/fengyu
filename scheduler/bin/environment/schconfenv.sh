#!/bin/bash
function check_write() {
    if [ $# -lt 2 ]; then
        echo "Usage: check_write message to_file"
        return
    fi
    message="$1"
    to_file="$2"
    grep "^${message}$" "${to_file}" >>/dev/null 
    if [ $? -ne 0 ]
    then
        echo "${message}" >> "${to_file}"
    fi
}

file="$HOME/.bashrc"

check_write '#env for scheduler:START' "${file}"

check_write 'export JAVA_HOME=/usr/java/default' "${file}"
check_write 'export PATH=${JAVA_HOME}/bin:${PATH}' "${file}"
check_write 'export PATH=/opt/scheduler/bin:${PATH}' "${file}"

check_write '#env for scheduler:END' "${file}"

if [ ! -d /data ]; then
    mkdir /data
fi

if [ ! -d /data/scheduler ]; then
    mkdir /data/scheduler
fi

sudo chmod 777 /opt /data /data/scheduler