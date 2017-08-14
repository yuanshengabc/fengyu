#!/bin/bash
#变量定义
sqlname="yuansheng.sql"
host="127.0.0.1"
user="datamaster"
passwd="datA123!@#"
dbname="datamaster"

while   getopts  i:h:u:p:  opt
do
    if [ $opt = i ];then
        input="$OPTARG"
    elif [ $opt = h ];then
        host="$OPTARG"
    elif [ $opt = u ];then
        user="$OPTARG"
    elif [ $opt = p ];then
        passwd="$OPTARG"
    else
        exit
    fi
done

shellPaht=$(dirname $(readlink -f "$0"))
#执行Python将json化为sql
if [ $input ];then
    python $shellPaht/ontology_transform.py -i $input -o $sqlname
    if [ $? == 2 ];then
        exit
    fi
else
    echo "Please input a ontology file!"
    exit
fi

#导入sql文件到指定数据库
mysql -h$host -u$user -p$passwd $dbname < $sqlname
if [ $? != 0 ];then
    exit
fi

#删除sql
rm $sqlname