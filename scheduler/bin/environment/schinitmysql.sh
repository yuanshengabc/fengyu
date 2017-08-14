#!/bin/bash

psw="datA123!@#"
mysql -uroot -p$psw -e"CREATE USER 'scheduler'@'%' IDENTIFIED BY '$psw';"
mysql -uroot -p$psw -e"grant all privileges on *.* to scheduler@'%' identified by '$psw' with grant option;"
mysql -uroot -p$psw -e"flush privileges;"

mysql -uscheduler -p$psw -e"source ${SCHPATH}/service/src/main/resources/sql/tables_mysql_innodb.sql"
