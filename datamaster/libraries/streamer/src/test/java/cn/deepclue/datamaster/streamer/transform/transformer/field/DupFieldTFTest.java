package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by xuzb on 14/04/2017.
 */
public class DupFieldTFTest {
    @Test
    public void testDupFieldTF() {
        RSSchema schema = new RSSchema();
        RSField field1 = new RSField("f1", BaseType.TEXT);
        schema.addField(field1);

        RSField field2 = new RSField("f2", BaseType.TEXT);
        schema.addField(field2);

        Record record = new Record(schema);
        record.addValue("abc");

        DupFieldTF tf = new DupFieldTF("f1", "f3");
        RSSchema targetSchema = tf.prepareSchema(schema);
        Record targetRecord = tf.transform(record);

        assertEquals(targetSchema.count(), schema.count() + 1);
        assertEquals(targetRecord.size(), record.size() + 1);

        assertEquals(targetSchema.getField(1).getName(), "f3");
        assertEquals(targetRecord.getValue(1), "abc");
    }

    @Test
    public void testMultiDupFieldTF() {
        RSSchema schema = new RSSchema();
        RSField field1 = new RSField("f1", BaseType.TEXT);
        schema.addField(field1);

        RSField field2 = new RSField("f1_COPY", BaseType.TEXT);
        schema.addField(field2);

        Record record = new Record(schema);
        record.addValue("abc");

        DupFieldTF tf = new DupFieldTF("f1", "");
        RSSchema targetSchema = tf.prepareSchema(schema);
        Record targetRecord = tf.transform(record);

        assertEquals(targetSchema.count(), schema.count() + 1);
        assertEquals(targetRecord.size(), record.size() + 1);

        assertEquals(targetSchema.getField(1).getName(), "f1_COPY1");
        assertEquals(targetRecord.getValue(1), "abc");
    }
}
