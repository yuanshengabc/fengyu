#!/bin/bash

cd /opt
if test -L ./dmcomponents/elasticsearch; then
    echo "Elasticsearch is already installed"
    exit
fi
find ./dmpackages -maxdepth 1 -name 'elasticsearch-2.*.tar.gz' | xargs tar zxf

find ./ -maxdepth 1 -type d -name 'elasticsearch-2.*' | xargs -I{} ln -s {} elasticsearch

cp /opt/dmpackages/conf/elasticsearch-logging.yml /opt/dmcomponents/elasticsearch/config/logging.yml

chown -R magneto.magneto /opt/dmcomponents/elasticsearch*
