package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by xuzb on 14/04/2017.
 */
public class SingleFieldTFTester {
    public static void test(SingleFieldTF tf, BaseType baseType, Object source, Object target) {
        RSSchema schema = new RSSchema();
        RSField field = new RSField(tf.getSourceFieldName(), baseType);
        schema.addField(field);

        Record record = new Record(schema);
        record.addValue(source);

        RSSchema targetSchema = tf.prepareSchema(schema);
        Record targetRecord = tf.transform(record);
        assertEquals(targetSchema.count(), schema.count());
        assertEquals(targetSchema.getField(0).getName(), schema.getField(0).getName());
        assertEquals(record.size(), targetRecord.size());

        Object newValue = targetRecord.getValue(0);
        assertEquals(target, newValue);
    }

    public static void test(Class<? extends SingleFieldTF> clazz, BaseType baseType, Object source, Object target) {

        SingleFieldTF tf = null;
        try {
            tf = clazz.getDeclaredConstructor(String.class).newInstance("f1");
        } catch (Exception ignored) {
        }

        if (tf == null) {
            assertFalse("Empty tf.", true);
            return;
        }

        test(tf, baseType, source, target);
    }
}
