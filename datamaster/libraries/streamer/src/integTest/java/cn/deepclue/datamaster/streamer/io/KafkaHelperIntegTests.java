package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

/**
 * Created by xuzb on 10/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaHelperIntegTests {

    @Autowired
    private StreamerIntegTestProperties properties;

    KTopicConfig toDeleteConfig;
    KTopicConfig toCreateConfig;

    @Before
    public void setUp() {
        toDeleteConfig = new KTopicConfig();
        toDeleteConfig.setKconfig(properties.getStandaloneKafkaConfig());
        toDeleteConfig.setTopic("kafka-helper-delete-test");

        KafkaHelper.createTopic(toDeleteConfig);

        toCreateConfig = new KTopicConfig();
        toCreateConfig.setKconfig(properties.getStandaloneKafkaConfig());
        toCreateConfig.setTopic("kafka-helper-create-test");

        KafkaHelper.deleteTopic(toCreateConfig);
    }

    @Test
    public void deleteTopic() {
        assertTrue(KafkaHelper.deleteTopic(toDeleteConfig));
    }

    @Test
    public void createTopic() {
        assertTrue(KafkaHelper.createTopic(toCreateConfig));
        KafkaHelper.deleteTopic(toCreateConfig);
    }
}
