package cn.deepclue.datamaster.streamer.io.kafka;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.deepclue.datamaster.streamer.Env.properties;

/**
 * Created by ggchangan on 17-7-13.
 */
public class KafkaUtils {
    private static final String topic = "kafka_integ_test_topic";

    public static RSSchema createRSSchema() {
        final String schemaName = "kafkaTestSchema";
        RSSchema schema = new RSSchema();
        schema.setName(schemaName);
        schema.addField(new RSField("int_field", BaseType.INT));
        schema.addField(new RSField("long_field", BaseType.LONG));
        schema.addField(new RSField("float_field", BaseType.FLOAT));
        schema.addField(new RSField("double_field", BaseType.DOUBLE));
        schema.addField(new RSField("text_field", BaseType.TEXT));
        schema.addField(new RSField("date_field", BaseType.DATE));

        return schema;
    }

    public static List<Record> createRecords(RSSchema schema) {
        Record record1 = new Record(schema);
        record1.addValue(1);
        record1.addValue(2L);
        record1.addValue(0.26f);
        record1.addValue(Math.random());
        record1.addValue("test text");
        record1.addValue(new Date());

        Record record2 = new Record(schema);
        record2.addValue(2);
        record2.addValue(50L);
        record2.addValue(4.26f);
        record2.addValue(Math.random());
        record2.addValue("test text");
        record2.addValue(new Date());

        List<Record> records = new ArrayList<>();

        records.add(record1);
        records.add(record2);

        return records;
    }

    public static KafkaConfig standAlone() {
        return properties.getStandaloneKafkaConfig();
    }

    public static KafkaConfig cluster() {
        return properties.getClusterKafkaConfig();
    }

    public static KTopicConfig createTopicConfig(KafkaConfig kafkaConfig, String topic) {
        KTopicConfig kTopicConfig = new KTopicConfig();

        kTopicConfig.setKconfig(kafkaConfig);
        kTopicConfig.setTopic(topic);
        int partitions = 1;
        kTopicConfig.setPartitions(partitions);
        int replicationFactor = 1;
        kTopicConfig.setReplicationFactor(replicationFactor);

        return kTopicConfig;
    }

    public static KTopicConfig createTopicConfig(KafkaConfig kafkaConfig) {
        return createTopicConfig(kafkaConfig, topic);
    }

}
