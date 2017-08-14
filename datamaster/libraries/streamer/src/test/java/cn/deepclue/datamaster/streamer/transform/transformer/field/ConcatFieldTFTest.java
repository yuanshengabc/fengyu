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
public class ConcatFieldTFTest {
    @Test
    public void testConcatFieldTF() {
        RSSchema schema = new RSSchema();
        RSField field = new RSField("f1", BaseType.TEXT);
        schema.addField(field);

        field = new RSField("f2", BaseType.TEXT);
        schema.addField(field);

        Record record = new Record(schema);
        record.addValue("abc");
        record.addValue("bcd");

        ConcatFieldTF tf = new ConcatFieldTF("f2", "f1", "f2 - f1", "-");
        RSSchema targetSchema = tf.prepareSchema(schema);
        Record targetRecord = tf.transform(record);

        assertEquals(targetSchema.count(), schema.count() + 1);
        assertEquals(targetRecord.size(), record.size() + 1);

        assertEquals(targetSchema.getField(2).getName(), "f2 - f1");
        assertEquals(targetRecord.getValue(2), "bcd-abc");
    }
}
