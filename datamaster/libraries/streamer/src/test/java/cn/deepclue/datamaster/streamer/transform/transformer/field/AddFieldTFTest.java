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
public class AddFieldTFTest {
    @Test
    public void testAddFieldTF() {
        RSSchema schema = new RSSchema();
        RSField field1 = new RSField("f1", BaseType.TEXT);
        schema.addField(field1);

        RSField field2 = new RSField("f2", BaseType.TEXT);
        schema.addField(field2);

        Record record = new Record(schema);
        record.addValue("abc");
        record.addValue(null);

        AddFieldTF tf = new AddFieldTF("f1", "f3", BaseType.TEXT, "bc");
        RSSchema targetSchema = tf.prepareSchema(schema);
        Record targetRecord = tf.transform(record);

        assertEquals(targetSchema.count(), schema.count() + 1);
        assertEquals(targetRecord.size(), record.size() + 1);

        assertEquals(targetSchema.getField(1).getName(), "f3");
        assertEquals(targetRecord.getValue(1), "bc");
    }
}
