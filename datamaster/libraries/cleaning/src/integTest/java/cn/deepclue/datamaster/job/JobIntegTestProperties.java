package cn.deepclue.datamaster.job;

import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Created by xuzb on 07/04/2017.
 */
@ConfigurationProperties
public class JobIntegTestProperties {
    @NestedConfigurationProperty
    private ESConfig esconfig = new ESConfig();

    @NestedConfigurationProperty
    private KafkaConfig kafkaConfig = new KafkaConfig();

    @NestedConfigurationProperty
    private MySQLConfig mysqlConfig = new MySQLConfig();

    @NestedConfigurationProperty
    private HDFSConfig hdfsConfig = new HDFSConfig();


    public ESConfig getEsconfig() {
        return esconfig;
    }

    public void setEsconfig(ESConfig esconfig) {
        this.esconfig = esconfig;
    }


    public KafkaConfig getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

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
