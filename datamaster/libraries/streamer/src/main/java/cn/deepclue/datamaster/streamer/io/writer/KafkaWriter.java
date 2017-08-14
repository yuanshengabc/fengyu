package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.exception.KafkaException;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.RecordConverter;
import cn.deepclue.datamaster.streamer.io.SchemaConverter;
import cn.deepclue.datamaster.streamer.session.KafkaZkSession;
import cn.deepclue.datamaster.streamer.util.HttpExecutor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xuzb on 29/03/2017.
 */
public class KafkaWriter implements Writer {

    private KTopicConfig config;
    private Schema avroSchema;
    private RSSchema rsSchema;
    private KafkaProducer<String, GenericRecord> producer;

    public KafkaWriter(KTopicConfig config) {
        this.config = config;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                KafkaHelper.setProducing(config, false);
            }
        });
    }

    private String getProducerId(String topic) {
        return topic + "-" + "producer-" + Thread.currentThread().getId();
    }

    public KTopicConfig getConfig() {
        return config;
    }

    private String buildSchemaPostUrl() {
        return "http://" + config.getSchemaRegServerString() + "/subjects/" + config.getTopic() + "-value/versions";
    }

    @Override
    public void writeSchema(RSSchema schema) {
        this.rsSchema = schema;
        this.avroSchema = SchemaConverter.toAvroSchemaByBuilder(schema);

        // Write kafka schema to Schema Registry by rest api.
        Map<String, String> properties = new Hashtable<>();
        properties.put("Content-Type", "application/vnd.schemaregistry.v1+json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schema", this.avroSchema.toString());
        String schemaString = jsonObject.toString();
        HttpExecutor.StringResp resp = HttpExecutor.doHttpPostRequest(buildSchemaPostUrl(), schemaString, properties);
        if (resp.isErrorCode()) {
            throw new KafkaException("Failed to write kafka schema.", "更新kafka schema失败。");
        }

        KafkaHelper.setProducing(config, true);
    }

    @Override
    public void close() {
        producer.close();
        KafkaHelper.setProducing(config, false);
    }

    @Override
    public void writeRecord(Record record) {
        GenericRecord avroRecord = RecordConverter.toAvroRecord(record, rsSchema, avroSchema);
        ProducerRecord<String, GenericRecord> producerRecord = new ProducerRecord<>(config.getTopic(), record.getKey(), avroRecord);
        producer.send(producerRecord);
    }

    @Override
    public void open() {
        Properties properties = new Properties();
        properties.put("client.id", getProducerId(config.getTopic()));
        properties.put("bootstrap.servers", KafkaZkSession.brokerString(config.getKconfig().getZkConfig()));
        properties.put("acks", "all");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        properties.put("schema.registry.url", "http://" + config.getSchemaRegServerString());

        producer = new KafkaProducer<>(properties);
    }
}
