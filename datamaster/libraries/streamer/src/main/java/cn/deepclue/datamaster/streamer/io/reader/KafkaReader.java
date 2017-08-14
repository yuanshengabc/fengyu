package cn.deepclue.datamaster.streamer.io.reader;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.ZkConfig;
import cn.deepclue.datamaster.streamer.exception.KafkaException;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.RecordConverter;
import cn.deepclue.datamaster.streamer.io.SchemaConverter;
import cn.deepclue.datamaster.streamer.session.KafkaZkSession;
import cn.deepclue.datamaster.streamer.util.HttpExecutor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by xuzb on 29/03/2017.
 */
public class KafkaReader implements Reader {

    private static Logger logger = LoggerFactory.getLogger(KafkaReader.class);
    protected KafkaConsumer<String, GenericRecord> consumer;
    protected KTopicConfig config;
    private ConsumerRecords<String, GenericRecord> cachedRecords;
    private Iterator<ConsumerRecord<String, GenericRecord>> cachedRecordIter;
    private RSSchema rsSchema;
    private Schema avroSchema;
    private String groupIdSuffix = "";
    private KafkaZkSession zkSession;

    private static final int MAX_RETRY_TIMES = 20;
    private int schemaReadRetryTimes = 0;
    private int noRecordPollTimes = 0;
    private long lastNoRecordPollTimes = System.currentTimeMillis();

    private static final int MAX_NO_RECORD_TIMES = 20;
    private static final int MAX_NO_RECORD_INTEVAL = 60 * 1000; //ms

    private int numRead = 0;


    public KafkaReader(KTopicConfig config) {
        this.config = config;
        ZkConfig zkConfig = new ZkConfig();
        zkConfig.setZkUrl(config.getZkServer());
        zkSession = new KafkaZkSession(zkConfig);
    }

    private String getConsumerGroupId(String topic) {
        return topic + "kafka-reader-group" + groupIdSuffix;
    }

    String getConsumerId(String topic) {
        return topic + "-consumer";
    }

    private String getUniqueID() {
        return UUID.randomUUID().toString();
    }

    public void setGroupIdSuffix(String groupIdSuffix) {
        this.groupIdSuffix = groupIdSuffix;
    }

    @Override
    public RSSchema readSchema() {
        if (rsSchema == null) {
            avroSchema = readAvroSchema();
            if (avroSchema != null) {
                rsSchema = SchemaConverter.fromAvroSchema(avroSchema);
            }
        }

        return rsSchema;
    }

    private String buildSchemaQueryUrl() {
        return "http://" + config.getSchemaRegServerString() + "/subjects/" + config.getTopic() + "-value/versions/latest";
    }

    private Schema readAvroSchema() {
        schemaReadRetryTimes = 0;
        while (avroSchema == null) {
            HttpExecutor.StringResp resp = HttpExecutor.doHttpGetRequest(buildSchemaQueryUrl());
            if (resp.isSuccessCode()) {
                String jsonContent = resp.getMessage();
                JSONObject jsonObject = new JSONObject(jsonContent);
                String schemaString = jsonObject.getString("schema");
                avroSchema = SchemaConverter.toAvroSchemaFromJson(schemaString);
            } else {
                ++schemaReadRetryTimes;
                logger.warn("Resetting consumer because we cannot read avro schema from topic {}!", config.getTopic());

                if (schemaReadRetryTimes > MAX_RETRY_TIMES) {
                    throw new KafkaException("Failed to read kafka schema.", "读取kafka元信息失败。");
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.warn("Failed to interrupt schema read thread {}.", e);
                    Thread.currentThread().interrupt();
                }
            }

        }

        return avroSchema;
    }

    private ConsumerRecords<String, GenericRecord> pollRecords() {
        return consumer.poll(1000);
    }

    private boolean isReachNoRecordLimit() {
        if (noRecordPollTimes < MAX_NO_RECORD_TIMES &&
                System.currentTimeMillis() - lastNoRecordPollTimes < MAX_NO_RECORD_INTEVAL) {
            noRecordPollTimes++;
            return false;
        }

        return true;
    }

    private void resetNoRecordFlags() {
        noRecordPollTimes = 0;
        lastNoRecordPollTimes = System.currentTimeMillis();
    }

    @Override
    public boolean hasNext() {
        if (cachedRecordIter != null && cachedRecordIter.hasNext()) {
            return true;
        }

        while (true) {
            if (numRead % 100000 == 0) {
                commitSync();
            }

            // Poll new records
            cachedRecords = pollRecords();
            cachedRecordIter = cachedRecords.iterator();
            if (cachedRecordIter.hasNext()) {
                resetNoRecordFlags();
                return true;
            }

            // Check if we are at the end of the topic
            Set<TopicPartition> topicPartitions = consumer.assignment();
            if (topicPartitions.isEmpty()) {
                resetConsumer();
                continue;
            }

            boolean isEnd = true;
            for (TopicPartition partitionInfo : topicPartitions) {
                long partitionOffset = zkSession.fetchOffset(partitionInfo.topic(), partitionInfo.partition());
                long consumerOffset = consumer.position(partitionInfo);

                if (partitionOffset != consumerOffset) {
                    isEnd = false;
                    break;
                }
            }

            // Here we reach at the end of the topic and the producing process ended.
            if (isEnd) {
                if (isReachNoRecordLimit()) {
                    logger.warn("No record limit");
                    throw new KafkaException("Cannot poll records from kafka topic " + config.getTopic(),
                            "从kafka主题：" + config.getTopic() + "无法轮询数据");
                }

                logger.info("Kafka topic {} offset ends, checking for more records.", config.getTopic());
                if (!KafkaHelper.isProducing(config)) {
                    logger.info("Kafka partitions: {}", topicPartitions.size());
                    return false;
                }
            }
        }
    }

    @Override
    public Record readRecord() {
        numRead++;

        ConsumerRecord<String, GenericRecord> consumerRecord = cachedRecordIter.next();
        GenericRecord genericRecord = consumerRecord.value();
        Record record = RecordConverter.fromAvroRecord(genericRecord, rsSchema);
        record.setKey(consumerRecord.key());

        return record;
    }

    private void resetConsumer() {
        if (consumer != null) {
            consumer.close();
        }

        subscribe();
    }

    @Override
    public void open() {
        rsSchema = readSchema();

        subscribe();
    }

    private void subscribe() {
        String brokersString = zkSession.brokersString();
        Properties props = new Properties();
        props.put("client.id", getConsumerId(config.getTopic()));
        props.put("group.id", getConsumerGroupId(config.getTopic()));
        //props.put("bootstrap.servers", config.getBrokerServerString());
        props.put("bootstrap.servers", brokersString);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        props.put("schema.registry.url", "http://" + config.getSchemaRegServerString());
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", false);

        consumer = new KafkaConsumer<>(props);

        doSubscribe();
    }

    protected void doSubscribe() {
        consumer.subscribe(Collections.singletonList(config.getTopic()));
    }

    @Override
    public void close() {
        if (consumer != null) {
            commitSync();
            consumer.close();
        }
    }

    private void commitSync() {
        try {
            consumer.commitSync();
        } catch (WakeupException e) {
            consumer.commitSync();
            logger.error("Commit offset failed {}", e);
        } catch (CommitFailedException e) {
            logger.error("Commit offset failed {}", e);
        }
    }
}
