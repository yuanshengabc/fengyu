#!/bin/bash

if [ ! $3 ];then
  sh deploy.sh $1 $2 restart
else
  sh deploy.sh $1 $2 $3 restart
fi
