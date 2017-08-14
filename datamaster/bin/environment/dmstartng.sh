#!/bin/bash
lc=`ps -ef | grep nginx | wc -l`
if [ $lc -gt 1 ]; then
    echo "nignx has been started"
else
    nginx
fi

