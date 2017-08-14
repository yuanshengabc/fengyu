#!/bin/bash

cd /opt
if test -L ./dmcomponents/confluent; then
    echo "Confluent platform is already installed."
    exit
fi
find ./dmpackages -maxdepth 1 -name 'confluent-*.tar.gz' | xargs tar zxf

find ./ -maxdepth 1 -type d -name 'confluent-*' | xargs -I{} ln -s {} confluent

echo "delete.topic.enable = true" >> /opt/dmcomponents/confluent/etc/kafka/server.properties
echo "log.retention.hours = 720" >> /opt/dmcomponents/confluent/etc/kafka/server.properties

chown -R magneto.magneto  /opt/dmcomponents/confluent*

