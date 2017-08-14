package cn.deepclue.datamaster.streamer;

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
public class StreamerIntegTestProperties {
    @NestedConfigurationProperty
    private ESConfig esconfig = new ESConfig();

    @NestedConfigurationProperty
    private KafkaConfig standaloneKafkaConfig = new KafkaConfig();

    @NestedConfigurationProperty
    private KafkaConfig clusterKafkaConfig = new KafkaConfig();

    @NestedConfigurationProperty
    private MySQLConfig mysqlConfig = new MySQLConfig();

    @NestedConfigurationProperty
    private HDFSConfig standaloneHDFSConfig = new HDFSConfig();

    @NestedConfigurationProperty
    private HDFSConfig clusterHDFSConfig = new HDFSConfig();

    public ESConfig getEsconfig() {
        return esconfig;
    }

    public void setEsconfig(ESConfig esconfig) {
        this.esconfig = esconfig;
    }


    public KafkaConfig getStandaloneKafkaConfig() {
        return standaloneKafkaConfig;
    }

    public void setStandaloneKafkaConfig(KafkaConfig standaloneKafkaConfig) {
        this.standaloneKafkaConfig = standaloneKafkaConfig;
    }

    public KafkaConfig getClusterKafkaConfig() {
        return clusterKafkaConfig;
    }

    public void setClusterKafkaConfig(KafkaConfig clusterKafkaConfig) {
        this.clusterKafkaConfig = clusterKafkaConfig;
    }

    public MySQLConfig getMysqlConfig() {
        return mysqlConfig;
    }

    public void setMysqlConfig(MySQLConfig mysqlConfig) {
        this.mysqlConfig = mysqlConfig;
    }

    public HDFSConfig getStandaloneHDFSConfig() {
        return standaloneHDFSConfig;
    }

    public void setStandaloneHDFSConfig(HDFSConfig standaloneHDFSConfig) {
        this.standaloneHDFSConfig = standaloneHDFSConfig;
    }

    public HDFSConfig getClusterHDFSConfig() {
        return clusterHDFSConfig;
    }

    public void setClusterHDFSConfig(HDFSConfig clusterHDFSConfig) {
        this.clusterHDFSConfig = clusterHDFSConfig;
    }
}
