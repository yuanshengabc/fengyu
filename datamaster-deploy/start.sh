#!/bin/bash
if [ ! $3 ];then
  sh deploy.sh $1 $2 start
else
  sh deploy.sh $1 $2 $3 start
fi
