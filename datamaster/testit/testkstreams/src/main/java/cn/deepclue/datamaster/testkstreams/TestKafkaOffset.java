package cn.deepclue.datamaster.testkstreams;

import cn.deepclue.datamaster.testkstreams.helper.OffsetFetcher;
import com.google.gson.Gson;
import io.confluent.common.utils.zookeeper.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

/**
 * Created by xuzb on 28/03/2017.
 */
public class TestKafkaOffset {
    private static String kafkapath = "/opt/confluent";
    private static String kafkabinpath = kafkapath + "/bin";

    public static void main(String[] args) {

        new Thread() {
            @Override
            public void run() {
                execute(1);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                execute(2);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                execute(3);
            }
        }.start();
    }

    static void execute(int i) {
        Properties props = new Properties();
        props.put("client.id", "testoffset"); // Should get host name.
        props.put("group.id", "testoffset3_group"); // Hard code a group id.
        props.put("bootstrap.servers", "lc16:9092");
        props.put("zookeeper.connect", "lc16:2181");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("schema.registry.url", "http://lc16:8081");
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", true);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Collections.singletonList("testoffset1"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(i + ": " + new Gson().toJson(record));
            }

            Set<TopicPartition> topicPartitions = consumer.assignment();
            OffsetFetcher offsetFetcher = new OffsetFetcher("lc16", 9092);

            boolean isEnd = true;
            for (TopicPartition partitionInfo : topicPartitions) {
                long toffset = offsetFetcher.fetch(partitionInfo);
                long offset = consumer.position(partitionInfo);

                if (toffset != offset) {
                    isEnd = false;
                    break;
                }
            }

            if (isEnd) {
                break;
            }
        }

        ZkClient zkClient = new ZkClient("lc16:2181");
        ZkUtils.updatePersistentPath(zkClient, "/producers/testoffset1", "true");
        System.out.println(ZkUtils.readData(zkClient, "/producers/testoffset1").getData());
    }
}
