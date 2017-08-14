package cn.deepclue.datamaster.fusion;

import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Created by xuzb on 07/04/2017.
 */
@ConfigurationProperties
public class FusionTestConfigurationProperties {
    @NestedConfigurationProperty
    private MySQLConfig mysqlConfig = new MySQLConfig();

    @NestedConfigurationProperty
    private HDFSConfig hdfsConfig = new HDFSConfig();

    public MySQLConfig getMysqlConfig() {
        return mysqlConfig;
    }

    public void setMysqlConfig(MySQLConfig mysqlConfig) {
        this.mysqlConfig = mysqlConfig;
    }

    public HDFSConfig getHdfsConfig() {
        return hdfsConfig;
    }

    public void setHdfsConfig(HDFSConfig hdfsConfig) {
        this.hdfsConfig = hdfsConfig;
    }
}
