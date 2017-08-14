#!/bin/bash

if [ ! -d /opt/dmpgs ]; then
    mkdir /opt/dmpgs
fi

if [ ! -d /opt/dmpgs/mysql ]; then
    mkdir /opt/dmpgs/mysql
fi

cd /opt/dmpackages/mysql/

yum install * -y
systemctl start mariadb.service
systemctl enable mariabd.service
depsw=`grep 'temporary password' /var/log/mysqld.log | awk -F 'localhost:' '{print $2}' | awk -F ' ' '{print $1}'`
${BINPATH}/environment/mariadbinit.exp $depsw
