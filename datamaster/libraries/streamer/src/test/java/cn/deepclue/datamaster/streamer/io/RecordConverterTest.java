package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by xuzb on 29/03/2017.
 */
public class RecordConverterTest {

    private Record record;
    private RSSchema rsSchema;
    private Schema avroSchema;

    @Before
    public void setUp() {
        rsSchema = new RSSchema();
        record = new Record(rsSchema);

        rsSchema.setName("TestRSSchema");

        RSField field = new RSField();
        field.setBaseType(BaseType.INT);
        field.setName("f1");
        rsSchema.addField(field);
        record.addValue(1);

        field = new RSField();
        field.setBaseType(BaseType.LONG);
        field.setName("f2");
        rsSchema.addField(field);
        record.addValue(1l);

        field = new RSField();
        field.setBaseType(BaseType.FLOAT);
        field.setName("f3");
        rsSchema.addField(field);
        record.addValue(1.0f);

        field = new RSField();
        field.setBaseType(BaseType.DOUBLE);
        field.setName("f4");
        rsSchema.addField(field);
        record.addValue(1.0d);

        field = new RSField();
        field.setBaseType(BaseType.DATE);
        field.setName("f5");
        rsSchema.addField(field);
        record.addValue(new Date());


        field = new RSField();
        field.setBaseType(BaseType.TEXT);
        field.setName("f6");
        rsSchema.addField(field);
        record.addValue("test");

        avroSchema = SchemaConverter.toAvroSchemaByBuilder(rsSchema);
    }

    @Test
    public void toAvroRecord() {
        GenericRecord genericRecord = RecordConverter.toAvroRecord(record, rsSchema, avroSchema);
        for (Map.Entry<String, Object> entry : record.getValueMap().entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            if (rsSchema.getField(fieldName).getBaseType() == BaseType.DATE) {
                Object recordValue = genericRecord.get(SchemaConverter.base32Encode(fieldName));
                assertEquals((long) recordValue, ((Date)value).getTime());
            } else {
                assertEquals(genericRecord.get(SchemaConverter.base32Encode(fieldName)), value);
            }
        }
    }

    @Test
    public void fromAvroRecord() {
        GenericRecord genericRecord = RecordConverter.toAvroRecord(record, rsSchema, avroSchema);
        Record record = RecordConverter.fromAvroRecord(genericRecord, rsSchema);
        for (Map.Entry<String, Object> entry : record.getValueMap().entrySet()) {
            RSField field = rsSchema.getField(entry.getKey());
            if (field.getBaseType() == BaseType.DATE) {
                assertEquals(((Date) entry.getValue()).getTime(), genericRecord.get(SchemaConverter.base32Encode(entry.getKey())));
            } else {
                assertEquals(entry.getValue(), genericRecord.get(SchemaConverter.base32Encode(entry.getKey())));
            }
        }
    }
}
