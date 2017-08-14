#!/bin/bash
if [ ! $3 ];then
  sh deploy.sh $1 $2 stop
else
  sh deploy.sh $1 $2 $3 stop
fi
