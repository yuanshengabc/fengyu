package cn.deepclue.datamaster.testio.base;

import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.rwstreamer.*;
import cn.deepclue.datamaster.testio.MatrixPrint;
import cn.deepclue.datamaster.testio.ioConfig.IOProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/23.
 */
@Configuration
@EnableConfigurationProperties(IOProperties.class)
public class IOStreamerBase {

    @Autowired
    public IOProperties ioProperties;

    protected MySQLTableConfig importConfig;

    protected KTopicConfig kTopicConfig;

    protected ESTypeConfig esTypeConfig;

    protected KTopicConfig k2kTopicConfig;

    protected MySQLTableConfig exportConfig;

    protected HDFSFileConfig hdfsFileConfig;

    public IOStreamerBase() {
    }

    public void init(String importTableName) {
        importConfig = new MySQLTableConfig();
        importConfig.setMysqlConfig(ioProperties.getMysqlImport());
        if (MatrixPrint.YML.equals(importTableName)) {
            importConfig.setTableName(ioProperties.getIoBean().getImportTableName());
        } else {
            importConfig.setTableName(importTableName);
        }

        kTopicConfig = new KTopicConfig();
        kTopicConfig.setKconfig(ioProperties.getKafkaConfig());
        kTopicConfig.setTopic(MatrixPrint.getKafkaTopic(MatrixPrint.KAFKA_IO_TOPIC_HEAD));

        esTypeConfig = ioProperties.getEsTypeConfig();
        esTypeConfig.setIndex(MatrixPrint.getESIndex(MatrixPrint.ES_IO_INDEX_HEAD));
        esTypeConfig.setType(MatrixPrint.getESType(MatrixPrint.ES_IO_TYPE_HEAD));

        k2kTopicConfig = new KTopicConfig();
        k2kTopicConfig.setKconfig(ioProperties.getKafkaConfig());
        k2kTopicConfig.setTopic(MatrixPrint.getKafkaTopic(MatrixPrint.KAFKA_IO_K2K_TOPIC_HEAD));

        exportConfig = new MySQLTableConfig();
        exportConfig.setMysqlConfig(ioProperties.getMysqlExport());
        exportConfig.setTableName(MatrixPrint.getMysqlTableName("export_", ioProperties.getIoBean().getRecordNumber(), ioProperties.getIoBean().getRecordColumn()));

        hdfsFileConfig = new HDFSFileConfig();
        hdfsFileConfig.setHdfsConfig(ioProperties.getHdfsConfig());
        hdfsFileConfig.setFilePath(MatrixPrint.getFilePath(MatrixPrint.HDFS_IO_FILEPATH_HEAD));
    }

    public void mySQL2Kafka() {
        MySQL2KafkaRWStreamer mySQL2Kafka = new MySQL2KafkaRWStreamer(importConfig, kTopicConfig);
        MatrixPrint.printTestStart("MySQL2KafkaRWStreamer:");
        MatrixPrint.print("    Table source:" + importConfig.getTableName());
        MatrixPrint.print("    Kafka topick:  " + kTopicConfig.getTopic());
        MatrixPrint.printDetails(ioProperties.getIoBean().getRecordNumber(), ioProperties.getIoBean().getRecordColumn());
        long startTimeM2k = MatrixPrint.getCurrentTimeLong();
        mySQL2Kafka.start();
        MatrixPrint.printRealRecordNum(mySQL2Kafka.getNProcessedRecords());
        MatrixPrint.printResultTime(startTimeM2k);
        MatrixPrint.printReslut("MySQL2Kafka", ioProperties.getIoBean().getRecordNumber(), startTimeM2k);
    }

    public void kafka2ES() {
        Kafka2ESRWStreamer kafka2ES = new Kafka2ESRWStreamer(kTopicConfig, esTypeConfig);
        MatrixPrint.printTestStart("Kafka2ESRWStreamer:");
        MatrixPrint.print("    ES index:  " + esTypeConfig.getIndex() + "  ,type:  " + esTypeConfig.getType());
        MatrixPrint.printDetails(ioProperties.getIoBean().getRecordNumber(), ioProperties.getIoBean().getRecordColumn());
        long startTimeK2e = MatrixPrint.getCurrentTimeLong();
        kafka2ES.start();
        MatrixPrint.printRealRecordNum(kafka2ES.getNProcessedRecords());
        MatrixPrint.printResultTime(startTimeK2e);
        MatrixPrint.printReslut("Kafka2ES", ioProperties.getIoBean().getRecordNumber(), startTimeK2e);
    }

    public void kafkaStreams() {
        KafkaStreamsRWStreamer kafkaStreams = new KafkaStreamsRWStreamer(kTopicConfig, k2kTopicConfig);
        MatrixPrint.printTestStart("KafkaStreamsRWStreamer:");
        MatrixPrint.print("    Kafka topick2:  " + k2kTopicConfig.getTopic());
        MatrixPrint.printDetails(ioProperties.getIoBean().getRecordNumber(), ioProperties.getIoBean().getRecordColumn());
        long startTimeK2k = MatrixPrint.getCurrentTimeLong();
        kafkaStreams.start();
        MatrixPrint.printRealRecordNum(kafkaStreams.getNProcessedRecords());
        MatrixPrint.printResultTime(startTimeK2k);
        MatrixPrint.printReslut("KafkaStreams", ioProperties.getIoBean().getRecordNumber(), startTimeK2k);
    }

    public void kafka2MySQL() {
        Kafka2MySQLRWStreamer kafka2MySQL = new Kafka2MySQLRWStreamer(k2kTopicConfig, exportConfig);
        MatrixPrint.printTestStart("Kafka2MySQLRWStreamer:");
        MatrixPrint.print("    Mysql tableName:  " + exportConfig.getTableName());
        MatrixPrint.printDetails(ioProperties.getIoBean().getRecordNumber(), ioProperties.getIoBean().getRecordColumn());
        long startTimeK2m = MatrixPrint.getCurrentTimeLong();
        kafka2MySQL.start();
        MatrixPrint.printRealRecordNum(kafka2MySQL.getNProcessedRecords());
        MatrixPrint.printResultTime(startTimeK2m);
        MatrixPrint.printReslut("Kafka2MySQL", ioProperties.getIoBean().getRecordNumber(), startTimeK2m);
    }

    public void kafka2HDFS() {
        Kafka2HDFSRWStreamer kafka2HDFS = new Kafka2HDFSRWStreamer(kTopicConfig, hdfsFileConfig);
        MatrixPrint.printTestStart("Kafka2HDFSRWStreamer:");
        MatrixPrint.print("HDFS file path:" + hdfsFileConfig.getFilePath());
        MatrixPrint.printDetails(ioProperties.getIoBean().getRecordNumber(), ioProperties.getIoBean().getRecordColumn());
        long startTimeK2H = MatrixPrint.getCurrentTimeLong();
        kafka2HDFS.start();
        MatrixPrint.printRealRecordNum(kafka2HDFS.getNProcessedRecords());
        MatrixPrint.printResultTime(startTimeK2H);
        MatrixPrint.printReslut("Kafka2HDFS", ioProperties.getIoBean().getRecordNumber(), startTimeK2H);
    }
}
