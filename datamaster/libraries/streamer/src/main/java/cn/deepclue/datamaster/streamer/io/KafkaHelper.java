package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import io.confluent.common.utils.zookeeper.ZkUtils;
import kafka.admin.TopicCommand;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetResponse;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.common.security.JaasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuzb on 30/03/2017.
 */
public class KafkaHelper {

    private static Logger logger = LoggerFactory.getLogger(KafkaHelper.class);

    private static final String PRODUCING = "0";
    private static final String PRODUCED = "1";
    public static void setProducing(KTopicConfig config, boolean producing) {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient(config.getZkServer());
            ZkUtils.updatePersistentPath(zkClient, getProducersPath(config), producing ? PRODUCING : PRODUCED);
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }

    public static boolean isProducing(KTopicConfig config) {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient(config.getZkServer());
            String data = ZkUtils.readData(zkClient, getProducersPath(config)).getData();
            return data == null || data.equals(PRODUCING);
        } catch (Exception e) {
            logger.error("Failed to check zookeeper persistent path: {}", e);
            return true;
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }

    private static String getProducersPath(KTopicConfig config) {
        return "/producers/" + config.getTopic();
    }

    public static boolean deleteTopic(KTopicConfig config) {
        TopicCommand.TopicCommandOptions options = new TopicCommand.TopicCommandOptions(new String [] {
                "--zookeeper",
                config.getZkServer(),
                "--delete",
                "--topic",
                config.getTopic()
        });

        kafka.utils.ZkUtils zkUtils = null;
        try {
            zkUtils = kafka.utils.ZkUtils.apply(config.getZkServer(), 30000, 30000, JaasUtils.isZkSecurityEnabled());
            TopicCommand.deleteTopic(zkUtils, options);
            return true;
        } catch (Exception e) {
            logger.info("Failed to delete topic {}. {}", config.getTopic(), e);
            return false;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
    }

    public static boolean createTopic(KTopicConfig config) {
        TopicCommand.TopicCommandOptions options = new TopicCommand.TopicCommandOptions(new String [] {
                "--zookeeper",
                config.getZkServer(),
                "--partitions",
                String.valueOf(config.getPartitions()),
                "--replication-factor",
                String.valueOf(config.getReplicationFactor()),
                "--create",
                "--topic",
                config.getTopic()
        });

        kafka.utils.ZkUtils zkUtils = null;
        try {
            zkUtils = kafka.utils.ZkUtils.apply(config.getZkServer(), 30000, 30000, JaasUtils.isZkSecurityEnabled());
            TopicCommand.createTopic(zkUtils, options);
            return true;
        } catch (Exception e) {
            logger.info("Failed to create topic {}. {}", config.getTopic(), e);
            return false;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
    }

    public static long getLastOffset(kafka.javaapi.consumer.SimpleConsumer consumer, String topic, int partition,
                                     long whichTime, String clientName) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
                requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            System.out.println("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition) );
            return 0;
        }

        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }
}
