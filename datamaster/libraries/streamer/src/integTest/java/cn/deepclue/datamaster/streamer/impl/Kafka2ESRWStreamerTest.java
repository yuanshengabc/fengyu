package cn.deepclue.datamaster.streamer.impl;

import cn.deepclue.datamaster.streamer.config.kafka.Worker;
import cn.deepclue.datamaster.streamer.config.kafka.sink.ESConnectorSink;
import cn.deepclue.datamaster.streamer.config.kafka.source.KafkaSource;
import cn.deepclue.datamaster.streamer.impl.kconnector.Kafka2MySQLStreamer;
import cn.deepclue.datamaster.streamer.impl.kconnector.KafkaStreamer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class Kafka2ESRWStreamerTest {
    private KafkaStreamer kafkaStreamer;
    private KafkaSource source;
    private ESConnectorSink sink;
    private Worker worker;

    @Before
    public void before() {
        kafkaStreamer = new Kafka2MySQLStreamer();
        worker = new Worker("localhsot:9092");
        List<String> list = new ArrayList<>();
        list.add("mytest");
        source = new KafkaSource(list, "localhsot:9092");
        sink = new ESConnectorSink("essink", "http://localhost:9200");

        kafkaStreamer.addSink(sink);
        kafkaStreamer.addSource(source);
    }

    @Test
    public void addConnector() {
        assertEquals(kafkaStreamer.getSource().getClass(), source.getClass());
        assertEquals(kafkaStreamer.getSink().getClass(), sink.getClass());
    }

    @Test
    public void getTempFile() {

        assertNotNull(kafkaStreamer.getTempFile(source.getTopics() + "\n" + sink.toPropertyString()));
        assertNotNull(kafkaStreamer.getTempFile(worker.toWorkerString()));
    }

    //    @Test
    public void start() {
        assertTrue(kafkaStreamer.start());
    }
}
