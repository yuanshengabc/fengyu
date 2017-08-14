package cn.deepclue.datamaster.streamer.config;

public class HDFSFileConfig implements Config {
    private String filePath;
    private HDFSConfig hdfsConfig;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
