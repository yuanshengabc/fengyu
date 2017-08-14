#!/bin/bash
if [ ! $3 ];then
  sh deploy.sh $1 $2 status
else
  sh deploy.sh $1 $2 $3 status
fi
