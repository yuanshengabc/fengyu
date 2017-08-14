package cn.deepclue.datamaster.streamer.impl;

import cn.deepclue.datamaster.streamer.config.kafka.sink.MySQLConnectorSink;
import cn.deepclue.datamaster.streamer.config.kafka.source.KafkaSource;
import cn.deepclue.datamaster.streamer.impl.kconnector.Kafka2MySQLStreamer;
import cn.deepclue.datamaster.streamer.impl.kconnector.KafkaStreamer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class Kafka2MySQLStreamerTest {
    private KafkaStreamer kafkaStreamer;
    private KafkaSource source;
    private MySQLConnectorSink sink;

    @Before
    public void before() {
        kafkaStreamer = new Kafka2MySQLStreamer();
        List<String> list = new ArrayList<>();
        list.add("mytest");
        source = new KafkaSource(list, "lc15:9092");
        sink = new MySQLConnectorSink("mysqlsink", "jdbc:mariadb://172.24.63.40/mytest?user=root&password=data123!@#", "b_${topic}");

        kafkaStreamer.addSource(source);
        kafkaStreamer.addSink(sink);
    }

    @Test
    public void addConnector() {
        assertEquals(kafkaStreamer.getSource().getClass(), source.getClass());
        assertEquals(kafkaStreamer.getSink().getClass(), sink.getClass());
    }

    @Test
    public void getTempFile() {
        assertNotNull(kafkaStreamer.getTempFile(source.getBootstrapServers() + "\n" + sink.toPropertyString()));
    }

    //    @Test
    public void start() {
        assertTrue(kafkaStreamer.start());
    }
}
