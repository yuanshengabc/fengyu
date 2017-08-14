package cn.deepclue.datamaster.fusion;

import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Created by xuzb on 23/05/2017.
 */
@ConfigurationProperties
public class FusionIntegTestProperties {

    @NestedConfigurationProperty
    private HDFSConfig hdfsConfig;

    public HDFSConfig getHdfsConfig() {
        return hdfsConfig;
    }

    public void setHdfsConfig(HDFSConfig hdfsConfig) {
        this.hdfsConfig = hdfsConfig;
    }
}
