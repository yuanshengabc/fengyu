package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.SchemaConverter;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaUtils;
import cn.deepclue.datamaster.streamer.io.kafka.KafkaWriterSession;
import cn.deepclue.datamaster.streamer.session.KafkaZkSession;
import cn.deepclue.datamaster.streamer.util.HttpExecutor;
import org.apache.avro.Schema;
import org.json.JSONObject;
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
public class KafkaWriterTest {
    private KafkaWriter kafkaWriter;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWriteSchema() {
        KafkaConfig kafkaConfig = KafkaUtils.standAlone();
        KTopicConfig kTopicConfig = KafkaUtils.createTopicConfig(kafkaConfig);
        kafkaWriter = new KafkaWriter(kTopicConfig);
        kafkaWriter.open();

        RSSchema schema = KafkaUtils.createRSSchema();
        kafkaWriter.writeSchema(schema);

        RSSchema retSchema = SchemaConverter.fromAvroSchema(getSchema(kTopicConfig));
        assertThat(retSchema.getName()).isEqualToIgnoringCase(schema.getName());
        assertThat(retSchema.getFields().size()).isEqualTo(6);
        assertThat(retSchema.getField("int_field").getBaseType()).isEqualTo(BaseType.INT);
        assertThat(retSchema.getField("long_field").getBaseType()).isEqualTo(BaseType.LONG);
        assertThat(retSchema.getField("float_field").getBaseType()).isEqualTo(BaseType.FLOAT);
        assertThat(retSchema.getField("double_field").getBaseType()).isEqualTo(BaseType.DOUBLE);
        assertThat(retSchema.getField("text_field").getBaseType()).isEqualTo(BaseType.TEXT);
        assertThat(retSchema.getField("date_field").getBaseType()).isEqualTo(BaseType.DATE);

        KafkaHelper.deleteTopic(kTopicConfig);
        kafkaWriter.close();
    }

    @Test
    public void testWriteRecordToKafkaWithSingleMachine() {
        KafkaConfig kafkaConfig = KafkaUtils.standAlone();
        KTopicConfig kTopicConfig = KafkaUtils.createTopicConfig(kafkaConfig);

        KafkaWriterSession session = new KafkaWriterSession(kTopicConfig);
        session.write();

        KafkaZkSession zkSession = new KafkaZkSession(session.getZkServer());
        long total = zkSession.totalRecords(session.getTopic());

        assertThat(total).isEqualTo(session.getRecords().size());

        session.clear();
    }

    @Test
    public void testWriteRecordToKafkaCluster() {
        KafkaConfig kafkaConfig = KafkaUtils.cluster();
        KTopicConfig kTopicConfig = KafkaUtils.createTopicConfig(kafkaConfig);

        KafkaWriterSession session = new KafkaWriterSession(kTopicConfig);
        session.write();

        KafkaZkSession zkSession = new KafkaZkSession(session.getZkServer());
        long total = zkSession.totalRecords(session.getTopic());
        assertThat(total).isEqualTo(session.getRecords().size());

        session.clear();
    }

    public Schema getSchema(KTopicConfig kTopicConfig) {

        Schema avroSchema = null;
        HttpExecutor.StringResp resp = HttpExecutor.doHttpGetRequest(buildSchemaQueryUrl(kTopicConfig));
        if (resp.isSuccessCode()) {
            String jsonContent = resp.getMessage();
            JSONObject jsonObject = new JSONObject(jsonContent);
            String schemaString = jsonObject.getString("schema");
            avroSchema = SchemaConverter.toAvroSchemaFromJson(schemaString);
        }

        return avroSchema;
    }

    private String buildSchemaQueryUrl(KTopicConfig kTopicConfig) {
        return "http://" + kTopicConfig.getSchemaRegServerString() + "/subjects/" + kTopicConfig.getTopic() + "-value/versions/latest";
    }
}
