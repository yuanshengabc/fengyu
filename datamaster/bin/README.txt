部署启停服务等命令都是靠一下两个命令来执行
1.datamaster  xxx
2.dmscp  xxx
其中datamaster是针对于本机的操作，  dmscp是对其他机器（目前指55测试机）

此套脚本类似于天合的shell安装脚本
若是开发环境，先把拉取datamaster和datamaster-web的代码，并把dmpackages文件夹拷贝到/opt下（确保是magneto用户）。

执行顺序：

首先用root用户
执行/opt/datamaster/bin/environment/ 下的dmconfsys和install相关脚本（或直接installall）
（由于nginx和mysql是通过yum安装，且有installall选项，所以为了方便选择用root执行，但脚本里都会把相关文件的所属用户切换到magneto）

以上完成后切换到magneto：
执行/opt/datamaster/bin/dmconfenv/
之后所有脚本都可以以这种方式执行 ： datamaster  xxx

执行datamaster  startall   （但不会启动清洗工具的后端服务）
datamaster  build
datamaster  buildweb
datamaster  start
datamaster  startweb
自此，清洗工具安装启动完成。

停服务可以类比于启动。


datamaster xxx使用说明：

其中xxx可以是install、start、stop、build等相关命令。
install: installall 、installnginx、installcp、installes、installmysql
start：startall（但不包括清洗工具后端的服务）、startes、startcp、startnginx、startweb、start（启动后端服务）
stop： stopall、stopes、stopcp、stopnginx、stop（停后端服务）
说明：
        1.installmysql会直接对mysql进行初始化配置，并且已经将mysql服务开启，且设置为开机自启动。目前没开启root用户的远程连接权限，而会新建datamaster用户且开启权限，
        且密码都是  datA123!@#  。
        2.常用的命令应该是构建启停前后端服务，因此设计start、stop对应后端相应操作、前端操作则在后面加上web，例如startweb、buildweb。
          而startweb只是重读了nginx的配置文件，就相当于重启了，因此没有stopweb选项，或者说相当于stopningx。
	3.如果是启动后端服务，那么还需要再加第二个参数设置环境，有dev、beta、prod三个选项。例如： datamaster start beta  。如果没有第二个参数，默认是dev环境。

dmscp xxx说明：
	1.目前只有 start、stop、build三个参数选择。
	2.dmscp start 与datamaster start用法一样，不多赘述。
	3.根据目前的环境暂时没开发指定ip的功能，未来在ansible上会有此功能，且将添加install等功能。
