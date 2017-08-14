package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaUtils;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaWriterSession;
import cn.deepclue.datamaster.streamer.io.rwstreamer.Kafka2ESRWStreamer;
import cn.deepclue.datamaster.streamer.session.ESSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by ggchangan on 17-6-28.
 * 测试环境，集群lc11, lc12
 * lc11环境配置如下：
 *  ip: 172.24.8.111
 * lc12环境配置如下：
 *  ip: 172.24.8.112
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Kafka2ESRWStreamerTest {
    private final String lc11 = "172.24.8.111";
    private final String esHostIp = lc11;
    private final String esClusterName = "datamaster";
    private final String esIndex = "kafka2esindex";
    private final String esType =  "kafka2estype";
    private ESSession esSession;
    ESConfig esConfig = new ESConfig();

    KafkaConfig kafkaConfig;
    KTopicConfig source;

    private Kafka2ESRWStreamer streamsRWStreamer;

    @Before
    public void setUp() {
        kafkaConfig = KafkaUtils.cluster();
        source = KafkaUtils.createTopicConfig(kafkaConfig);

        KafkaWriterSession session = new KafkaWriterSession(source);
        session.write();
        KafkaHelper.setProducing(source, false);

        esConfig.setClusterName(esClusterName);
        esConfig.setClusterIp(esHostIp);
        esConfig.setShardsNum(5);
        esConfig.setReplicasNum(2);
        esConfig.setParallelism(1);
        ESTypeConfig sink = new ESTypeConfig(esConfig, esIndex, esType);

        streamsRWStreamer = new Kafka2ESRWStreamer(source, sink);
        esSession = new ESSession(esConfig);
    }

    @After
    public void teartDown() {
        KafkaHelper.deleteTopic(source);
        esSession.deleteIndex(esIndex);
        esSession.close();
    }

    @Test
    public void testStart() {
        streamsRWStreamer.start();

        //TODO
        //why need 1s waiting after es cluster has been refreshed by eswriter;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long totalDocs = esSession.totalDocs(esIndex, esType);
        assertThat(totalDocs).isEqualTo(2);
    }

    @Test
    public void testStartCluster() {

    }
}
