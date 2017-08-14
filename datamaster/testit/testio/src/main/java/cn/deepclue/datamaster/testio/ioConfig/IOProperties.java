package cn.deepclue.datamaster.testio.ioConfig;

import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import cn.deepclue.datamaster.testio.bean.IOBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Created by lilei-mac on 2017/4/13.
 */
@ConfigurationProperties
public class IOProperties {

    @NestedConfigurationProperty
    private ESTypeConfig esTypeConfig = new ESTypeConfig();

    @NestedConfigurationProperty
    private KafkaConfig kafkaConfig = new KafkaConfig();

    @NestedConfigurationProperty
    private MySQLConfig mysqlImport = new MySQLConfig();

    @NestedConfigurationProperty
    private MySQLConfig mysqlExport = new MySQLConfig();

    @NestedConfigurationProperty
    private HDFSConfig hdfsConfig = new HDFSConfig();

    @NestedConfigurationProperty
    private IOBean ioBean = new IOBean();

    @NestedConfigurationProperty
    private RSSchema rsSchema = new RSSchema();

    public ESTypeConfig getEsTypeConfig() {
        return esTypeConfig;
    }

    public void setEsTypeConfig(ESTypeConfig esTypeConfig) {
        this.esTypeConfig = esTypeConfig;
    }

    public KafkaConfig getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public MySQLConfig getMysqlImport() {
        return mysqlImport;
    }

    public void setMysqlImport(MySQLConfig mysqlImport) {
        this.mysqlImport = mysqlImport;
    }

    public MySQLConfig getMysqlExport() {
        return mysqlExport;
    }

    public void setMysqlExport(MySQLConfig mysqlExport) {
        this.mysqlExport = mysqlExport;
    }

    public HDFSConfig getHdfsConfig() {
        return hdfsConfig;
    }

    public void setHdfsConfig(HDFSConfig hdfsConfig) {
        this.hdfsConfig = hdfsConfig;
    }

    public IOBean getIoBean() {
        return ioBean;
    }

    public void setIoBean(IOBean ioBean) {
        this.ioBean = ioBean;
    }

    public RSSchema getRsSchema() {
        return rsSchema;
    }

    public void setRsSchema(RSSchema rsSchema) {
        this.rsSchema = rsSchema;
    }
}