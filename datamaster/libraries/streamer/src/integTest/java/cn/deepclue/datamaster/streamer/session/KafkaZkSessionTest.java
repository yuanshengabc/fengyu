package cn.deepclue.datamaster.streamer.session;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.config.ZkConfig;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaUtils;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaWriterSession;
import kafka.cluster.BrokerEndPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by ggchangan on 17-7-12.
 */
public class KafkaZkSessionTest {
    private String lc11 = "172.24.8.111";
    private int port = 2181;
    private ZkConfig zkConfig = new ZkConfig();
    private KafkaZkSession zkSession;

    @Before
    public void setUp() throws Exception {
        String zkUrl = String.format("%s:%d", lc11, port);
        zkConfig.setZkUrl(zkUrl);
        zkSession = new KafkaZkSession(zkConfig);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void findAllBrokers() throws Exception {
        List<BrokerEndPoint> brokers = zkSession.findAllBrokers();
        assertEquals(2, brokers.size());
    }

    @Test
    public void brokersString() throws Exception {
        String brokersString = zkSession.brokersString();
        assertEquals("lc11:9092,lc12:9092", brokersString);
    }

    @Test
    public void totalRecordsStandAlone() throws Exception {
        KafkaConfig kafkaConfig = KafkaUtils.standAlone();
        KTopicConfig kTopicConfig = KafkaUtils.createTopicConfig(kafkaConfig);

        KafkaWriterSession session = new KafkaWriterSession(kTopicConfig);
        session.clear();
        session.write();

        KafkaZkSession zkSession = new KafkaZkSession(zkConfig(session.getZkServer()));
        long total = zkSession.totalRecords(session.getTopic());

        assertThat(total).isEqualTo(session.getRecords().size());

        session.clear();
    }

    @Test
    public void totalRecordsCluster() throws Exception {
        KafkaConfig kafkaConfig = KafkaUtils.cluster();
        KTopicConfig kTopicConfig = KafkaUtils.createTopicConfig(kafkaConfig);

        KafkaWriterSession session = new KafkaWriterSession(kTopicConfig);
        session.clear();
        session.write();

        long total = zkSession.totalRecords(session.getTopic());

        assertThat(total).isEqualTo(session.getRecords().size());

        session.clear();
    }

    private static ZkConfig zkConfig(String host, int port) {
        String zkUrl = String.format("%s:%d", host, port);
        ZkConfig zkConfig = new ZkConfig();
        zkConfig.setZkUrl(zkUrl);
        return zkConfig;
    }

    private static ZkConfig zkConfig(String zkUrl) {
        ZkConfig zkConfig = new ZkConfig();
        zkConfig.setZkUrl(zkUrl);
        return zkConfig;
    }
}
