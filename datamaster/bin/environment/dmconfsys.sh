#!/bin/bash

if [ ! -d /data ]; then
    mkdir /data
fi

chmod 777 /data /data/log /opt

# yum install -y ntp kernel-tools sysstat dstat iotop subversion
if [ -d "/opt/ext/os/centos_install/ntp" ]; then
    cd /opt/ext/os/centos_install/ntp
    sh install
else 
    echo "no /opt/ext/os/centos_install/ntp found."
    exit
fi

if [ -d "/opt/ext/os/centos_install/kernel-tools" ]; then
    cd /opt/ext/os/centos_install/kernel-tools
    sh install
else
    echo "no /opt/ext/os/centos_install/kernel-tools found."
    exit
fi

if [ -d "/opt/ext/os/centos_install/iotop" ]; then
    cd /opt/ext/os/centos_install/iotop
    sh install
else
    echo "no /opt/ext/os/centos_install/iotop found."
    exit
fi

if [ -d "/opt/ext/os/centos_install/sysstat" ]; then
    cd /opt/ext/os/centos_install/sysstat
    sh install
else
    echo "no /opt/ext/os/centos_install/sysstat found."
    exit
fi

if [ -d "/opt/ext/os/centos_install/subversion" ]; then
    cd /opt/ext/os/centos_install/subversion
    sh install
else
    echo "no /opt/ext/os/centos_install/subversion found."
    exit
fi


#end of yum install -y ntp kernel-tools sysstat dstat iotop subversion
systemctl start ntpd
systemctl enable ntpd

systemctl stop firewalld
systemctl disable firewalld

if ! grep -q 'memlock unlimited' /etc/security/limits.conf; then
    echo '* - memlock unlimited' >> /etc/security/limits.conf
fi

if ! grep -q 'nofile 1048576' /etc/security/limits.conf; then
    echo '* - nofile 1048576'     >> /etc/security/limits.conf
fi
if ! grep -q 'nproc 999999' /etc/security/limits.conf; then
    echo '* - nproc 999999'       >> /etc/security/limits.conf
fi
if ! grep -q 'as unlimited' /etc/security/limits.conf; then
    echo '* - as unlimited'      >> /etc/security/limits.conf
fi
if ! grep -q 'fsize unlimited' /etc/security/limits.conf; then
    echo '* - fsize unlimited'   >> /etc/security/limits.conf
fi

if ! grep -q 'nproc 999999' /etc/security/limits.d/90-nproc.conf; then
    echo '* - nproc 999999' >>  /etc/security/limits.d/90-nproc.conf
fi

if ! grep -q 'max_map_count' /etc/sysctl.conf; then
    echo 'vm.max_map_count = 16777216' >> /etc/sysctl.conf
fi

if ! grep -q 'overcommit_memory' /etc/sysctl.conf; then
    echo 'vm.overcommit_memory = 1' >> /etc/sysctl.conf
fi

if ! grep -q 'somaxconn' /etc/sysctl.conf; then
    echo 'net.core.somaxconn = 1024' >> /etc/sysctl.conf
fi

sysctl -p

# This setting avoids the cpu running at a very low frequency.
cpupower frequency-set --governor performance
if ! grep -q 'cpupower' /etc/rc.local; then
    echo 'cpupower frequency-set --governor performance' >> /etc/rc.local
fi

# This setting is required by redis to have low latency. Redis must be restart after setting this.
echo never > /sys/kernel/mm/transparent_hugepage/enabled
if ! grep -q 'hugepage' /etc/rc.local; then
    echo 'echo never > /sys/kernel/mm/transparent_hugepage/enabled' >> /etc/rc.local
fi

chmod +x /etc/rc.d/rc.local

echo "magneto ALL=(ALL)       ALL"  >> /etc/sudoers