package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by xuzb on 14/04/2017.
 */
public class SplitFieldTFTest {

    private void testSplitFieldTF(String value, String sep, int splitCount, List<String> expects) {
        RSSchema schema = new RSSchema();
        RSField field1 = new RSField("f1", BaseType.TEXT);
        schema.addField(field1);

        RSField field2 = new RSField("f2", BaseType.TEXT);
        schema.addField(field2);

        Record record = new Record(schema);
        record.addValue(value);
        record.addValue(value);

        SplitFieldTF tf = new SplitFieldTF("f1", splitCount, sep);
        RSSchema targetSchema = tf.prepareSchema(schema);
        Record targetRecord = tf.transform(record);

        assertEquals(targetSchema.count(), schema.count() + splitCount);
        assertEquals(targetRecord.size(), record.size() + splitCount);

        for (int i = 1, j = 0; i < splitCount + 1; ++i, ++j) {
            assertEquals(targetSchema.getField(i).getName(), "f1_" + (j + 1));
            assertEquals(targetRecord.getValue(i), expects.get(j));
        }

        SplitFieldTF tf2 = new SplitFieldTF("f1", splitCount, sep);
        RSSchema newTargetSchema = tf2.prepareSchema(targetSchema);
        assertEquals(newTargetSchema.count(), targetSchema.count() + splitCount);
    }

    @Test
    public void testSplitFieldTF() {
        testSplitFieldTF("abc-123", "-", 2, Arrays.asList("abc", "123"));
        testSplitFieldTF("abc-123-cde", "-", 2, Arrays.asList("abc", "123-cde"));
        testSplitFieldTF("abc-123", "-", 3, Arrays.asList("abc", "123", null));
        testSplitFieldTF("abc.123", ".", 2, Arrays.asList("abc", "123"));
        testSplitFieldTF("abc*123", "*", 2, Arrays.asList("abc", "123"));
    }
}
