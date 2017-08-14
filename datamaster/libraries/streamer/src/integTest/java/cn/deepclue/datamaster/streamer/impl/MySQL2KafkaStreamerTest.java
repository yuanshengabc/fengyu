package cn.deepclue.datamaster.streamer.impl;

import cn.deepclue.datamaster.streamer.config.kafka.Worker;
import cn.deepclue.datamaster.streamer.config.kafka.sink.KafkaSink;
import cn.deepclue.datamaster.streamer.config.kafka.source.MySQLConnectorSource;
import cn.deepclue.datamaster.streamer.impl.kconnector.KafkaStreamer;
import cn.deepclue.datamaster.streamer.impl.kconnector.MySQL2KafkaStreamer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MySQL2KafkaStreamerTest {
    private KafkaStreamer kafkaStreamer;
    private MySQLConnectorSource source;
    private KafkaSink sink;
    private Worker worker;

    @Before
    public void before() {
        source = new MySQLConnectorSource("mysqlsource", "jdbc:mariadb://172.24.63.40/mytest?user=root&password=root", "id");
        sink = new KafkaSink("si_", "lc15:9092");
        worker = new Worker("localhost:9092");
        kafkaStreamer = new MySQL2KafkaStreamer();

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
        assertNotNull(kafkaStreamer.getTempFile(source.toPropertyString() + "\n" + sink.getTopicPrefix()));
        assertNotNull(kafkaStreamer.getTempFile(worker.toWorkerString()));
    }

    //    @Test
    public void start() {
        assertTrue(kafkaStreamer.start());
    }
}
