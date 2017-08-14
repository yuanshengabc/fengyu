#!/usr/bin/env bash

#lib path
base=${DMPATH}/genlibs/
sparklibs=${DMPATH}/genlibs/sparklibs/

#spark dependency packages
spark_avro_path=`ls ${sparklibs}spark-avro*`
datamaster_streamer_path=`ls ${sparklibs}streamer*`
commons_codec_path=`ls ${sparklibs}commons-codec*`