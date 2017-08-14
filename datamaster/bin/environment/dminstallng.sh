#!/bin/bash
cd /opt/dmpackages/nginx
yum install * -y

cp /opt/dmpackages/conf/default.conf  /etc/nginx/conf.d/default.conf
chmod u+s /usr/sbin/nginx
chown -R magneto.magneto /etc/nginx/
