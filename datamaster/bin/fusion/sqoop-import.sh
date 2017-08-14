#!/usr/bin/env bash
hadoop=hadoop
$hadoop fs -test -e $5
if [ $? -ne 1 ]
then
    echo "Input directory: "$5" do exists!"
    $hadoop fs -rm -r $5
fi

base=${DMPATH}/genlibs/
sparklibs=${DMPATH}/genlibs/sparklibs/

columns=`java -cp ${sparklibs}*:${base}* cn.deepclue.datamaster.fusion.tool.DBColumnReader $1 $2 $3 $4`
query_prefix="SELECT CONCAT(\"$6\", @i:=@i+1) as uuid,"
query_suffix=" FROM \`$4\` a,(SELECT @i:=100000000000) AS b where \$CONDITIONS"
for i in $columns
do
    origin_column=`python ${BINPATH}/fusion/base32_decode.py $i`
    query_prefix=$query_prefix"a.\`$origin_column\` as $i,"
done

query=${query_prefix:0:${#query_prefix}-1}${query_suffix}

sqoop import --connect $1 --username $2 --password $3 --query "$query" --target-dir $5 -m 1 --as-avrodatafile