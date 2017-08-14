package cn.deepclue.datamaster.streamer.config;

/**
 * Created by ggchangan on 17-7-7.
 */
public class ZkConfig {
    private String zkUrl;
    private Integer sessionTimeout = 5000;
    private Integer connectionTimeout = 5000;

    public String getZkUrl() {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
