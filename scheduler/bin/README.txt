environment/schconfenv.sh使用说明：
会配置权限，增加环境变量（需重启bash生效），创建目录等


scheduler $1 $2使用说明：

其中
$1可以是stop, build, installall, installmysql, initall, initmysql, start等相关命令。
$2是环境参数，当build时使用，默认为local
说明：
1.installmysql会直接对mysql进行初始化配置，并且已经将mysql服务开启，且设置为开机自启动。
目前没开启root用户的远程连接权限，密码是datA123!@#。
2.initmysql会新建scheduler用户且开启权限，且密码是datA123!@#，之后执行数据库结构创建脚本。