package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.io.reader.KafkaReader;
import cn.deepclue.datamaster.streamer.io.writer.KafkaWriter;
import cn.deepclue.datamaster.testio.MatrixPrint;
import cn.deepclue.datamaster.testio.base.ReadAndWriteBase;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/7.
 */
@Configuration
public class KafkaWriterTest extends ReadAndWriteBase {

    private String topic;

    public KafkaWriterTest(){
        this.topic= MatrixPrint.getKafkaTopic(MatrixPrint.KAFKA_TOPIC_HEAD);
    }

    public void testStart(){
        KTopicConfig topicConfig=new KTopicConfig();
        topicConfig.setTopic(topic);
        topicConfig.setKconfig(ioProperties.getKafkaConfig());

        KafkaWriter writer = new KafkaWriter(topicConfig);
        MatrixPrint.printTestStart("Kafka Writer:");
        MatrixPrint.print("    Kafka topic:  "+topic);
        testWriter(writer);
    }
}
