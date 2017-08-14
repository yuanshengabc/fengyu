#!/bin/bash
current_path=`pwd`
dmdeploy=${current_path}/playbooks
cd ${dmdeploy}
mods="initsys expect java mysql es cp confluent nginx datamaster datamasterweb module site dev_site mesos spark hdfs sqoop zookeeper hadoop"
tags="install conf init start restart clear status danger trigger_restart"

helplist=$'脚本使用方法如下：
例如：sh dmdeploy.sh lc17 es install
或者：sh dmdeploy.sh product lc17 es install'

#m,n in functions is the judgement condition
#modFun() is used to determine whether the user input the parameter of tag or mod is correct.

#判断是否输入了组件名称
modFun() {
	n=0
	for i in $mods
	do
		if [ $1 = $i ];then
			n=1
			break
		else
			:
		fi
	done
}

if [ ! $1 ];then
	echo "${helplist}"

elif [ ! $4 ];then
	modFun $2

	if [ $n != 1 ];then
		echo "Sorry, please input which module would  you want to control."

	elif [ $2 = initsys -o $2 = expect -o $2 = java -o $2 = mysql -o $2 = nginx -o $2 = datamasterweb -o $2 = site -o $2 = dev_site -o $2 = hdfs ];then
		ansible-playbook -i ${dmdeploy}/inventories/dm_develop/$1 ${2}.yml --tags="$3" --ask-sudo-pass

	elif [ $2 = hdfs -a $3 = init ];then
		ansible-playbook -i ${dmdeploy}/inventories/dm_develop/$1 ${2}.yml --tags="$3" --ask-sudo-pass

	elif [ $2 = hadoop -a $3 = conf ];then
         	 ansible-playbook -i ${dmdeploy}/inventories/dm_develop/$1 ${2}.yml --tags="$3" --ask-sudo-pass

	elif [ $2 = cp ];then
		ansible-playbook -i ${dmdeploy}/inventories/dm_develop/$1 confluent.yml --tags="$3"

	else
    ansible-playbook -i ${dmdeploy}/inventories/dm_develop/$1 ${2}.yml --tags="$3"
  fi

else
	modFun $3

	if [ $1 != product -a $1 != develop ];then
	  echo "Sorry, please input product or develop."

	elif [ $n != 1 ];then
    echo "Sorry, please input which module would  you want to control."

	elif [ $3 = initsys -o $3 = expect -o $3 = java -o $3 = mysql -o $3 = nginx -o $3 = datamasterweb -o $3 = site -o $3 = dev_site ];then
    ansible-playbook -i ${dmdeploy}/inventories/dm_${1}/$2 ${3}.yml --tags="$4" --ask-sudo-pass

	elif [ $3 = hdfs -a $4 = init ];then
	  ansible-playbook -i ${dmdeploy}/inventories/dm_${1}/$2 ${3}.yml --tags="$4" --ask-sudo-pass

	elif [ $3 = hadoop -a $4 = conf ];then
	  ansible-playbook -i ${dmdeploy}/inventories/dm_${1}/$2 ${3}.yml --tags="$4" --ask-sudo-pass

	elif [ $3 = cp ];then
	  ansible-playbook -i ${dmdeploy}/inventories/dm_${1}/$2 confluent.yml --tags="$4"

	else
		ansible-playbook -i ${dmdeploy}/inventories/dm_${1}/$2 ${3}.yml --tags="$4"
	fi

fi
