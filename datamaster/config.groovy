environments {
    //本地环境
    local {
        jdbc {//数据库
            ip = InetAddress.getLocalHost().getHostAddress()
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = InetAddress.getLocalHost().getHostAddress()
        }

        kafka {
            zkUrl = InetAddress.getLocalHost().getHostAddress()
            schemaRegServer = InetAddress.getLocalHost().getHostAddress()
        }

        hdfs {
            server = InetAddress.getLocalHost().getHostAddress()
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = InetAddress.getLocalHost().getHostAddress()
        }
    }

    // 开发环境
    dev {
        jdbc {//数据库
            ip = '172.24.8.117'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.117'
        }

        kafka {
            schemaRegServer = '172.24.8.117'
            zkUrl = '172.24.8.117'
        }

        hdfs {
            server = '172.24.8.117'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.117'
        }
    }

    //测试环境
    beta {
        jdbc {//数据库
            ip = '172.24.8.117'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.117'
        }

        kafka {
            schemaRegServer = '172.24.8.117'
            zkUrl = '172.24.8.117'
        }

        hdfs {
            server = '172.24.8.117'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.117'
        }
    }

    lc10 {
        jdbc {//数据库
            ip = '172.24.8.110'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.110'
        }

        kafka {
            schemaRegServer = '172.24.8.110'
            zkUrl = '172.24.8.110'
        }

        hdfs {
            server = '172.24.8.110'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.110'
        }
    }

    lc11_lc12{
        jdbc {//数据库
            ip = '172.24.8.111'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.111'
        }

        kafka{
            schemaRegServer='172.24.8.111'
            zkUrl ='172.24.8.111'
        }

        hdfs {
            server = ''
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.111:2181,172.24.8.112:2181'
        }
    }

    lc14 {
        jdbc {//数据库
            ip = '172.24.8.114'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.114'
        }

        kafka {
            schemaRegServer = '172.24.8.114'
            zkUrl = '172.24.8.114'
        }

        hdfs {
            server = '172.24.8.114'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.114'
        }
    }

    lc15{
        jdbc {//数据库
            ip = '172.24.8.115'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.115'
        }

        kafka {
            schemaRegServer = '172.24.8.115'
            zkUrl = '172.24.8.115'
        }

        hdfs {
            server = '172.24.8.115'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.115'
        }
    }

    lc16 {
        jdbc {//数据库
            ip = '172.24.8.116'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.116'
        }

        kafka {
            schemaRegServer = '172.24.8.116'
            zkUrl = '172.24.8.116'
        }

        hdfs {
            server = '172.24.8.116'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.116'
        }
    }

    lc17 {
        jdbc {//数据库
            ip = '172.24.8.117'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.117'
        }

        kafka {
            schemaRegServer = '172.24.8.117'
            zkUrl = '172.24.8.117'
        }

        hdfs {
            server = '172.24.8.117'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.117'
        }
    }

    db131 {
        jdbc {//数据库
            ip = '172.24.8.131'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.131'
        }

        kafka {
            schemaRegServer = '172.24.8.131'
            zkUrl = '172.24.8.131'
        }

        hdfs {
            server = '172.24.8.131'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.131'
        }
    }

    db134 {
        jdbc {//数据库
            ip = '172.24.8.134'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '172.24.8.131'
        }

        kafka {
            schemaRegServer = '172.24.8.134'
            zkUrl = '172.24.8.134'
        }

        hdfs {
            server = '172.24.8.134'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '172.24.8.134'
        }
    }

    xinghe {
        jdbc {//数据库
            ip = '10.10.10.84'
            username = 'datamaster'
            password = 'datA123!@#'
        }

        es {//elasticsearch
            clusterName = 'datamaster'
            clusterIp = '10.10.10.84'
        }

        kafka {
            schemaRegServer = '10.10.10.84'
            zkUrl = '10.10.10.84'
        }

        hdfs {
            server = '10.10.10.84'
            port = 9000
            clusterName = 'datamaster'
            username = 'magneto'
            zkUrl = '10.10.10.84'
        }
    }
}
