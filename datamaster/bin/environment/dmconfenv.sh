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


check_write '#env for datamaster:START' "${file}"

check_write 'export JAVA_HOME=/usr/java/default' "${file}"
check_write 'export PATH=${JAVA_HOME}/bin:${PATH}' "${file}"
check_write 'export PATH=/opt/datamaster/bin:${PATH}' "${file}"

check_write '#env for datamaster:END' "${file}"

mkdir /data/datamaster
mkdir /data/datamaster/nginx
touch /data/datamaster/nginx/cleaner.log

source $HOME/.bashrc
