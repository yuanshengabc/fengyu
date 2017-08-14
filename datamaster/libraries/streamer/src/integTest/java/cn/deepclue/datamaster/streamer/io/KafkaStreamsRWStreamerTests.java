package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.io.rwstreamer.KafkaStreamsRWStreamer;
import cn.deepclue.datamaster.streamer.io.writer.KafkaWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * Created by xuzb on 30/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaStreamsRWStreamerTests {

    @Autowired
    private StreamerIntegTestProperties properties;

    private KafkaStreamsRWStreamer streamsRWStreamer;
    private KafkaWriter writer;
    private RSSchema rsSchema;

    @Before
    public void setUp() {
        KafkaConfig kafkaConfig = properties.getStandaloneKafkaConfig();

        KTopicConfig source = new KTopicConfig();
        source.setKconfig(kafkaConfig);
        source.setTopic("rw-test1");

        KTopicConfig sink = new KTopicConfig();
        sink.setKconfig(kafkaConfig);
        sink.setTopic("rw-test2");

        streamsRWStreamer = new KafkaStreamsRWStreamer(source, sink);

        writer = new KafkaWriter(source);

        rsSchema = new RSSchema();
        rsSchema.setName("TestRSSchema");
        RSField field = new RSField();
        field.setBaseType(BaseType.INT);
        field.setName("f1");

        rsSchema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.LONG);
        field.setName("f2");

        rsSchema.addField(field);
    }

    @Test
    public void testStart() {
        writer.open();
        writer.writeSchema(rsSchema);
        Random random = new Random();
        for (int i = 0; i < 1; ++i) {
            Record record = new Record(rsSchema);
            record.addValue(random.nextInt());
            record.addValue(random.nextLong());
            writer.writeRecord(record);
        }

        writer.close();

        streamsRWStreamer.start();
    }
}
