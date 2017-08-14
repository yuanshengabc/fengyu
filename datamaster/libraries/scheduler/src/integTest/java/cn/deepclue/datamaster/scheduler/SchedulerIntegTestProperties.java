package cn.deepclue.datamaster.scheduler;

import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Created by xuzb on 07/04/2017.
 */
@ConfigurationProperties
public class SchedulerIntegTestProperties {
    @NestedConfigurationProperty
    private ESConfig esconfig = new ESConfig();

    @NestedConfigurationProperty
    private KafkaConfig kafkaConfig = new KafkaConfig();

    @NestedConfigurationProperty
    private MySQLConfig mysqlConfig = new MySQLConfig();


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
}
