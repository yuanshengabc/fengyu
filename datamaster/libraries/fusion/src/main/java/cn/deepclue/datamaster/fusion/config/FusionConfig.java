package cn.deepclue.datamaster.fusion.config;

import cn.deepclue.datamaster.streamer.config.Config;
import cn.deepclue.datamaster.streamer.config.HDFSConfig;

/**
 * Created by xuzb on 09/05/2017.
 */
public class FusionConfig implements Config {
    private String fusionKey;

    private HDFSConfig hdfsConfig;

    public String getFusionKey() {
        return fusionKey;
    }

    public void setFusionKey(String fusionKey) {
        this.fusionKey = fusionKey;
    }

    public HDFSConfig getHdfsConfig() {
        return hdfsConfig;
    }

    public void setHdfsConfig(HDFSConfig hdfsConfig) {
        this.hdfsConfig = hdfsConfig;
    }

    public String getHDFSServerString() {
        return hdfsConfig.getServerString();
    }
}
