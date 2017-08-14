package cn.deepclue.datamaster.streamer.config;

/**
 * Created by xuzb on 29/03/2017.
 */
public class KafkaConfig implements Config {
    private ZkConfig zkConfig;
    private String schemaRegServer;
    private int schemaRegPort = 8081;

    public ZkConfig getZkConfig() {
        return zkConfig;
    }

    public void setZkConfig(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    public String getSchemaRegServerString() {
        return schemaRegServer + ":" + schemaRegPort;
    }

    public void setSchemaRegServer(String schemaRegServer) {
        this.schemaRegServer = schemaRegServer;
    }

    public void setSchemaRegPort(int schemaRegPort) {
        this.schemaRegPort = schemaRegPort;
    }
}
