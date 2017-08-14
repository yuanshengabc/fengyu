package cn.deepclue.datamaster.streamer.io.kafka;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.writer.KafkaWriter;

import java.util.List;

/**
 * Created by ggchangan on 17-7-13.
 */
public class KafkaWriterSession {
    private KTopicConfig kTopicConfig;
    private List<Record> records;

    public KafkaWriterSession(KTopicConfig config) {
        this.kTopicConfig = config;
    }

    public void write() {
        KafkaWriter kafkaWriter = new KafkaWriter(kTopicConfig);
        kafkaWriter.open();
        RSSchema schema = KafkaUtils.createRSSchema();
        kafkaWriter.writeSchema(schema);

        records = KafkaUtils.createRecords(schema);

        for (Record record: records) {
            kafkaWriter.writeRecord(record);
        }
        kafkaWriter.close();
    }

    public void clear() {
        KafkaHelper.deleteTopic(kTopicConfig);
    }

    public List<Record> getRecords() {
        return records;
    }

    public String getTopic() {
        return kTopicConfig.getTopic();
    }

    public String getZkServer() {
        return kTopicConfig.getZkServer();
    }
}
