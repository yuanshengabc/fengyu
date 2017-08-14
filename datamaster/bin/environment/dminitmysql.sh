#!/bin/bash
psw="datA123!@#"
mysql -udatamaster -p$psw -e"CREATE DATABASE IF NOT EXISTS datamaster;"
mysql -udatamaster -p$psw -Ddatamaster -e"source ${DMPATH}/cleaner/src/integTest/resources/sql/drop.sql"
mysql -udatamaster -p$psw -Ddatamaster -e"source ${DMPATH}/cleaner/src/main/resources/sql/schema.sql"
mysql -udatamaster -p$psw -Ddatamaster -e"source ${DMPATH}/cleaner/src/integTest/resources/sql/data.sql"
mysql -udatamaster -p$psw -Ddatamaster -e"source ${DMPATH}/libraries/scheduler/src/main/resources/tables_mysql_innodb.sql"
