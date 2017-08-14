#!/bin/bash
localip=$1 || `ip addr | grep 'state UP' -A2 | tail -n1 | awk '{print $2}' | cut -f1 -d '/'`

#清空所有数据
curl -XDELETE "http://${localip}:9200/_all"
#建立test索引
curl -XPUT "http://${localip}:9200/ds_1_rs_1"
#建立type的mapping结构
curl -XPUT "http://${localip}:9200/ds_1_rs_1/ds_1_rs_1/_mapping" -d @${BINPATH}/environment/mapping.json
#索引数据
curl -XPOST "http://${localip}:9200/_bulk" --data-binary @${BINPATH}/environment/data.json

#查看索引
#GET _cat/indices
#查看mapping
#GET test/_mapping
#查找数据
#GET test/_search
#{
#    "query": {
#        "match_all": {}
#    }
#}
