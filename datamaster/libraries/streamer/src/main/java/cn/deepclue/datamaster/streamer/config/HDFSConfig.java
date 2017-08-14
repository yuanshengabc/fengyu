package cn.deepclue.datamaster.streamer.config;

import cn.deepclue.datamaster.streamer.io.HDFSHelper;

/**
 * Created by xuzb on 03/05/2017.
 */
public class HDFSConfig implements Config {
    private String server;
    private int port = 9000;
    private String clusterName;
    private String username;
    private ZkConfig zkConfig;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerString() {
        if (server == null || server.isEmpty()) {
            HDFSHelper.fetchServerFromZK(this);
        }
        return "hdfs://" + server + ":" + port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ZkConfig getZkConfig() {
        return zkConfig;
    }

    public void setZkConfig(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }
}
