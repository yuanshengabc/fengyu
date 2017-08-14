
# Deploy

Deploy system based on [Ansible](http://docs.ansible.com/ansible/)


# Usage

## inventories

配置部署环境的机器列表和分组信息，同时可以为每个组、每台机器配置相关的属性信息（变量）.  

以下是一个名为 test_dell 的环境的配置结构:
```
inventories/
`-- test_dell
    |-- group_vars
    |   |-- all.yml
    |   `-- cassandra.yml
    |-- hosts
    `-- host_vars
```

+ *hosts* 文件中配置机器列表和分组信息，该文件中也可以配置组以及单台机器的属性信息（变量）
+ *group_vars* 目录中配置组相关的属性信息（变量）, all 针对所有机器，cassandra 针对 hosts 中的 cassandra 组
+ *host_vars* 目录中可以配置针对某一台机器的属性信息（变量）

eg:
hosts:
```
[proj]
dell1

[cassandra]
172.24.63.22 is_seeds=1
172.24.63.23
172.24.63.26

[cassandra:vars]
cassandra_seeds="172.24.63.22"
```

cassandra.yml:
```
cassandra_cluster_name: "test_yuzc"
cassandra_listen_ip: "{{ ansible_em1.ipv4.address }}"
cassandra_rpc_port: 9160
cassandra_data_dirs:
  - /data01/cassandra/data
  - /data/cassandra/data
```


## roles

角色，可以简单的理解为一个模块即为一个角色，比如 cassandra，es 等.  
每个角色一个单独的目录，角色相关的所有 tasks, handlers, templates 等都保存在对应的目录内.


## playbooks

根目录下的 cassandra.yml 为 cassandra 的 playbook.  
playbook 一般是通过包含 roles 将对应的功能导入，如果不使用角色也可以单独写 tasks 和 handlers 来实现对应的功能。


site.yml 会配置安装所有组件.

init.yml 初始化安装系统环境.

java.yml 安装java使用tar包.

confsite.yml 配置site.json 文件以后业务模块解耦后可舍去.

web.yml 需要打包 x3和x3admin 文件夹成 web.tar.gz 放到packages 文件夹下

init.yml 需要 打包centos_install（原来是os文件夹下）文件夹所有依赖 centos_install.tar.gz 放置到packages 文件夹下

mgneto.yml 需要打包mgneto 文件夹 magneto.tar.gz 到packages 文件夹下


## usage

cd deploy

*install*
```
ansible-playbook -i inventories/test_dell/hosts cassandra.yml --tags="install"
```

*conf*
```
ansible-playbook -i inventories/test_dell/hosts cassandra.yml --tags="conf"
```

*start*
```
ansible-playbook -i inventories/test_dell/hosts cassandra.yml --tags="start"
```

*stop*
```
ansible-playbook -i inventories/test_dell/hosts cassandra.yml --tags="stop"
```

*restart*
```
ansible-playbook -i inventories/test_dell/hosts cassandra.yml --tags="restart"
```

*status*
```
ansible-playbook -i inventories/test_dell/hosts cassandra.yml --tags="status"
```
*with sudo*
```
ansible-playbook -i inventories/test_dell/hosts cassandra.yml --tags="status" --ask-sudo-pass
```
## *NOTICE*

ansible 通过 ssh 来实现远控功能，某些时候可能会 hang 死, 比如执行过程中 ctrl+C 等.  
如果出现 hang 死，先手工检查机器能否登录，如果可以登录，则检查所有的 sshd 进程，将对应用户的 sshd 进程全杀掉。  


[详细说明](http://wiki.clueos.cn/pages/viewpage.action?pageId=985212)
