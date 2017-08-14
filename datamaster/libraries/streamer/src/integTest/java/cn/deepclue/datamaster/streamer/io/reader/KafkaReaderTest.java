package cn.deepclue.datamaster.streamer.io.reader;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaUtils;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaWriterSession;
import cn.deepclue.datamaster.streamer.session.KafkaZkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by ggchangan on 17-6-23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaReaderTest {
    //TODO zhangren 测试，消费者可消费指定topic,指定偏移量的消息,添加偏移量处理逻辑
    private KafkaReader kafkaReader;


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * 被同组消费的数据再次启动时，会从上次commited的offset消费
     */
    @Test
    public void testReaderAgain() throws InterruptedException {

        KafkaConfig kafkaConfig = KafkaUtils.standAlone();
        KTopicConfig kTopicConfig = KafkaUtils.createTopicConfig(kafkaConfig);

        KafkaWriterSession session = new KafkaWriterSession(kTopicConfig);
        session.clear();

        session.write();

        KafkaZkSession zkSession = new KafkaZkSession(session.getZkServer());
        long total = zkSession.totalRecords(session.getTopic());

        assertThat(total).isEqualTo(session.getRecords().size());

        KafkaHelper.setProducing(kTopicConfig, false);


        //2. reader all records
        long t1 = System.currentTimeMillis();
        kafkaReader = new KafkaReader(kTopicConfig);
        long t11 = System.currentTimeMillis();
        kafkaReader.open();
        long t12 = System.currentTimeMillis();
        System.out.println(String.format("open time: %d", (t12 - t11)));
        RSSchema rsSchema = kafkaReader.readSchema();
        assertThat(rsSchema).isNotNull();
        long t13 = System.currentTimeMillis();
        System.out.println(String.format("read schema time: %d", (t13 - t12)));
        assertThat(kafkaReader.hasNext()).isTrue();
        long t14 = System.currentTimeMillis();
        System.out.println(String.format("first has next time: %d", (t14 - t13)));
        Record record1 = kafkaReader.readRecord();
        long t15 = System.currentTimeMillis();
        System.out.println(String.format("first read record time: %d", (t15 - t14)));
        assertThat(record1).isNotNull();
        assertThat(kafkaReader.hasNext()).isTrue();
        long t16 = System.currentTimeMillis();
        System.out.println(String.format("second has next time: %d", (t16 - t15)));
        Record record2 = kafkaReader.readRecord();
        long t17 = System.currentTimeMillis();
        System.out.println(String.format("second read record time: %d", (t17 - t16)));
        assertThat(record2).isNotNull();
        assertThat(kafkaReader.hasNext()).isFalse();
        long t18 = System.currentTimeMillis();
        System.out.println(String.format("third has next time: %d", (t18 - t17)));
        kafkaReader.close();
        long t19 = System.currentTimeMillis();
        System.out.println(String.format("close time: %d", (t19 - t18)));
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("第1次读取花费时间:%d", (t2 - t1)));

        //3. reader all records again
        kafkaReader = new KafkaReader(kTopicConfig);
        long t21 = System.currentTimeMillis();
        kafkaReader.open();
        long t22 = System.currentTimeMillis();
        System.out.println(String.format("second open time: %d", (t22 - t21)));
        rsSchema = kafkaReader.readSchema();
        assertThat(rsSchema).isNotNull();
        long t23 = System.currentTimeMillis();
        System.out.println(String.format("second read schema time: %d", (t23 - t22)));
        assertThat(kafkaReader.hasNext()).isFalse();
        long t24 = System.currentTimeMillis();
        System.out.println(String.format("final has next time: %d", (t24 - t23)));
        kafkaReader.close();
        long t25 = System.currentTimeMillis();
        System.out.println(String.format("second close time: %d", (t25 - t24)));
        long t3 = System.currentTimeMillis();
        System.out.println(String.format("第2次读取花费时间:%d", (t3 - t2)));

        session.clear();
    }

    @Test
    public void testKafkaStartOverReaderAgain() {
        KafkaConfig kafkaConfig = KafkaUtils.cluster();
        KTopicConfig kTopicConfig = KafkaUtils.createTopicConfig(kafkaConfig);

        KafkaWriterSession session = new KafkaWriterSession(kTopicConfig);
        session.clear();

        session.write();

        KafkaZkSession zkSession = new KafkaZkSession(session.getZkServer());
        long total = zkSession.totalRecords(session.getTopic());
        assertThat(total).isEqualTo(session.getRecords().size());

        KafkaHelper.setProducing(kTopicConfig, false);

        //2. reader all records
        kafkaReader = new KafkaStartOverReader(kTopicConfig);
        kafkaReader.open();
        RSSchema rsSchema = kafkaReader.readSchema();
        assertThat(rsSchema).isNotNull();
        assertThat(kafkaReader.hasNext()).isTrue();
        Record record1 = kafkaReader.readRecord();
        assertThat(record1).isNotNull();
        assertThat(kafkaReader.hasNext()).isTrue();
        Record record2 = kafkaReader.readRecord();
        assertThat(record2).isNotNull();
        assertThat(kafkaReader.hasNext()).isFalse();
        kafkaReader.close();

        //3. reader all records again
        kafkaReader = new KafkaStartOverReader(kTopicConfig);
        kafkaReader.open();
        rsSchema = kafkaReader.readSchema();
        assertThat(rsSchema).isNotNull();
        assertThat(kafkaReader.hasNext()).isTrue();
        record1 = kafkaReader.readRecord();
        assertThat(record1).isNotNull();
        assertThat(kafkaReader.hasNext()).isTrue();
        record2 = kafkaReader.readRecord();
        assertThat(record2).isNotNull();
        assertThat(kafkaReader.hasNext()).isFalse();
        kafkaReader.close();

        session.clear();
    }
}
