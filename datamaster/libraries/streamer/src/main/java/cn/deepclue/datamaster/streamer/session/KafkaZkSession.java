package cn.deepclue.datamaster.streamer.session;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.ZkConfig;
import kafka.admin.TopicCommand;
import kafka.cluster.Broker;
import kafka.cluster.BrokerEndPoint;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.protocol.SecurityProtocol;
import org.apache.kafka.common.security.JaasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.deepclue.datamaster.streamer.io.KafkaHelper.getLastOffset;

/**
 * Created by ggchangan on 17-7-7.
 */
public class KafkaZkSession {
    private static Logger logger = LoggerFactory.getLogger(KafkaZkSession.class);

    private ZkConfig zkConfig;
    private ZkUtils zkUtils;

    public KafkaZkSession(String zkUrl) {
        this(apply(zkUrl));
    }

    public KafkaZkSession(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
        this.zkUtils = ZkUtils.apply(zkConfig.getZkUrl(),
                        zkConfig.getSessionTimeout(),
                        zkConfig.getConnectionTimeout(),
                        JaasUtils.isZkSecurityEnabled());
    }

    public Integer findLeaderBroker(String topic, int partition) {
        Option<Object> leaderId = zkUtils.getLeaderForPartition(topic, partition);
        return (Integer) leaderId.get();
    }

    public long fetchOffset(String topic, int partition) {
        Integer leaderBroker = findLeaderBroker(topic, partition);
        BrokerEndPoint leaderBrokerInfo = findBrokerInfo(leaderBroker);
        String clientName = String.format("Client_%s_%d", topic, partition);
        SimpleConsumer consumer = new SimpleConsumer(leaderBrokerInfo.host(), leaderBrokerInfo.port(), 100000, 64 * 1024, clientName);
        return getLastOffset(consumer, topic, partition, kafka.api.OffsetRequest.LatestTime(), clientName);
    }

    public BrokerEndPoint findBrokerInfo(int brokerId) {
        Broker broker = zkUtils.getBrokerInfo(brokerId).get();
        BrokerEndPoint brokerEndPoint = broker.getBrokerEndPoint(SecurityProtocol.PLAINTEXT);

        return brokerEndPoint;
    }

    public List<BrokerEndPoint> findAllBrokers() {
        Seq<Broker> brokerSeq = zkUtils.getAllBrokersInCluster();
        List<Broker> brokers = JavaConversions.seqAsJavaList(brokerSeq);
        return brokers.stream().map(broker -> broker.getBrokerEndPoint(SecurityProtocol.PLAINTEXT)).collect(Collectors.toList());
    }

    public String brokersString() {
        return findAllBrokers().stream()
                .map(broker-> String.format("%s:%d",broker.host(), broker.port()))
                .collect(Collectors.joining(","));
    }

    public static String brokerString(ZkConfig zkConfig) {
        KafkaZkSession session = null;
        try {
            session = new KafkaZkSession(zkConfig);
            return session.brokersString();
        } finally {
            session.close();
        }
    }

    public long totalRecords(String topic) {
        Seq<String> topics = JavaConversions.asScalaBuffer(Collections.singletonList(topic)).toSeq();
        Option<Seq<Object>> partitions = zkUtils.getPartitionsForTopics(topics).get(topic);
        if (partitions.isEmpty()) {
            logger.warn(String.format("topic:%s doesn't exist!"));
            return 0;
        }

        return JavaConversions.seqAsJavaList(partitions.get()).stream()
                .map(p -> Integer.parseInt(p.toString()))
                .map(partition -> fetchOffset(topic, partition))
                .reduce(0l, Long::sum);
    }

    public void close() {
        if (zkUtils != null) {
            zkUtils.close();
        }
    }

    public boolean deleteTopic(String topic) {
        TopicCommand.TopicCommandOptions options = new TopicCommand.TopicCommandOptions(new String [] {
                "--zookeeper",
                zkConfig.getZkUrl(),
                "--delete",
                "--topic",
                topic
        });

        try {
            TopicCommand.deleteTopic(zkUtils, options);
            return true;
        } catch (Exception e) {
            logger.info("Failed to delete topic {}. {}", topic, e);
            return false;
        }
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

    public static ZkConfig apply(String zkUrl) {
        ZkConfig zkConfig = new ZkConfig();
        zkConfig.setZkUrl(zkUrl);
        return zkConfig;
    }

}
