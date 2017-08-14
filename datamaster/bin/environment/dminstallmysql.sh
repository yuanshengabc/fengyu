#!/bin/bash
cd /opt/dmpackages/mysql/
yum install * -y
systemctl start mariadb.service
systemctl enable mariabd.service
depsw=`grep 'temporary password' /var/log/mysqld.log | awk -F 'localhost:' '{print $2}' | awk -F ' ' '{print $1}'`
${BINPATH}/environment/mariadbinit.exp $depsw

psw="datA123!@#"
mysql -uroot -p$psw -e"CREATE USER 'datamaster'@'%' IDENTIFIED BY '$psw';"
mysql -uroot -p$psw -e"grant all privileges on *.* to datamaster@'%' identified by '$psw' with grant option;"
mysql -uroot -p$psw -e"flush privileges;"
