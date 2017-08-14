#!/bin/bash

if [ ! $3 ];then

  if [ $2 = mysql ];then
    sh deploy.sh $1 expect install
    sh deploy.sh $1 $2 install,conf,init
  elif [ $2 = hdfs ];then
    sh deploy.sh $1 hdfs install,conf
  elif [ $2 = hdfs_init ];then
		sh deploy.sh $1 hdfs install,conf,init
  else
    sh deploy.sh $1 $2 install,conf
  fi

else

  if [ $2 = mysql ];then
    sh deploy.sh $1 $2 expect install
    sh deploy.sh $1 $2 mysql install,conf,init
  else [ $3 = hdfs ];then
    sh deploy.sh $1 $2 hdfs install,conf
  elif [ $3 = hdfs_init ];then
    sh deploy.sh $1 $2 hdfs install,conf,init
  else
    sh deploy.sh $1 $2 $3 install,conf
  fi

fi
