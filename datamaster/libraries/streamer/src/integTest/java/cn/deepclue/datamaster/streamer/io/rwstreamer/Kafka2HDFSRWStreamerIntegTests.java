package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.io.HDFSClient;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.reader.HDFSReader;
import cn.deepclue.datamaster.streamer.io.writer.KafkaWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Kafka2HDFSRWStreamerIntegTests {
    private final String kafkaTopic = "kafka2hdfs_rwstreamer_integtests";
    private final String filePath = "/cleaning/kafka2hdfs_rwstreamer_integtests";
    @Autowired
    private StreamerIntegTestProperties properties;

    private Kafka2HDFSRWStreamer kafka2HDFSRWStreamer;
    private KafkaWriter kafkaWriter;
    private HDFSReader hdfsReader;
    private HDFSClient hdfsClient;

    private RSSchema rsSchema;
    private Record record1;
    private Record record2;

    @Before
    public void setUp() {
        load();

        init();
    }

    private void load() {
        KTopicConfig kTopicConfig = new KTopicConfig();
        kTopicConfig.setKconfig(properties.getStandaloneKafkaConfig());
        kTopicConfig.setTopic(kafkaTopic);
        kafkaWriter = new KafkaWriter(kTopicConfig);

        HDFSFileConfig hdfsFileConfig = new HDFSFileConfig();
        hdfsFileConfig.setHdfsConfig(properties.getStandaloneHDFSConfig());
        hdfsFileConfig.setFilePath(filePath);
        hdfsReader = new HDFSReader(hdfsFileConfig);
        hdfsClient = new HDFSClient(properties.getStandaloneHDFSConfig());

        kafka2HDFSRWStreamer = new Kafka2HDFSRWStreamer(kTopicConfig, hdfsFileConfig);
    }

    private void init() {
        rsSchema = new RSSchema();
        rsSchema.setName("test");
        rsSchema.addField(new RSField("int_field", BaseType.INT));
        rsSchema.addField(new RSField("long_field", BaseType.LONG));
        rsSchema.addField(new RSField("float_field", BaseType.FLOAT));
        rsSchema.addField(new RSField("double_field", BaseType.DOUBLE));
        rsSchema.addField(new RSField("text_field", BaseType.TEXT));
        rsSchema.addField(new RSField("date_field", BaseType.DATE));

        record1 = new Record(rsSchema);
        record1.addValue(1);
        record1.addValue(2L);
        record1.addValue(0.26f);
        record1.addValue(Math.random());
        record1.addValue("test text");
        record1.addValue(new Date());

        record2 = new Record(rsSchema);
        record2.addValue(2);
        record2.addValue(50L);
        record2.addValue(4.26f);
        record2.addValue(Math.random());
        record2.addValue("test text");
        record2.addValue(new Date());

        try {
            kafkaWriter.open();

            kafkaWriter.writeSchema(rsSchema);
            kafkaWriter.writeRecord(record1);
            kafkaWriter.writeRecord(record2);
        } finally {
            kafkaWriter.close();
        }
    }

    @Test
    public void test() {
        kafka2HDFSRWStreamer.start();

        try {
            hdfsReader.open();

            assertThat(hdfsReader.readSchema().count()).isEqualTo(rsSchema.count());
            assertThat(hdfsReader.hasNext()).isTrue();
            assertThat(hdfsReader.readRecord().size()).isEqualTo(record1.size());
            assertThat(hdfsReader.hasNext()).isTrue();
            assertThat(hdfsReader.readRecord().size()).isEqualTo(record2.size());
            assertThat(hdfsReader.hasNext()).isFalse();
        } finally {
            hdfsReader.close();
        }
    }

    @After
    public void tearDown() {
        KTopicConfig kTopicConfig = new KTopicConfig();
        kTopicConfig.setKconfig(properties.getStandaloneKafkaConfig());
        kTopicConfig.setTopic(kafkaTopic);
        KafkaHelper.setProducing(kTopicConfig, false);
        KafkaHelper.deleteTopic(kTopicConfig);

        try {
            hdfsClient.open();
            hdfsClient.deleteFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hdfsClient.close();
        }
    }
}
