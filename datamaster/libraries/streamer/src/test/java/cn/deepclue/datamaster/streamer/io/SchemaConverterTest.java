package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.apache.avro.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by xuzb on 29/03/2017.
 */
public class SchemaConverterTest {

    private RSSchema schema;

    @Before
    public void setUp() {
        schema = new RSSchema();
        schema.setName("TestRSSchema");
        RSField field = new RSField();
        field.setBaseType(BaseType.INT);
        field.setName("f1");

        schema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.LONG);
        field.setName("f2");

        schema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.FLOAT);
        field.setName("f3");

        schema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.DOUBLE);
        field.setName("f4");

        schema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.DATE);
        field.setName("f5");

        schema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.TEXT);
        field.setName("f6");

        schema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.TEXT);
        field.setName("f7_*");

        schema.addField(field);
    }

    @Test
    public void toAvroSchema() {
        Schema avroSchema = SchemaConverter.toAvroSchemaByBuilder(schema);
        assertEquals(avroSchema.getName(), SchemaConverter.base32Encode(schema.getName()));
        for (RSField field : schema.getFields()) {
            assertNotNull(avroSchema.getField(SchemaConverter.base32Encode(field.getName())));
        }


        avroSchema = SchemaConverter.toAvroSchemaByJson(schema);
        assertEquals(avroSchema.getName(), SchemaConverter.base32Encode(schema.getName()));
        for (RSField field : schema.getFields()) {
            assertNotNull(avroSchema.getField(SchemaConverter.base32Encode(field.getName())));
        }
    }

    @Test
    public void fromAvroSchema() {
        Schema avroSchema = SchemaConverter.toAvroSchemaByBuilder(schema);
        RSSchema genSchema = SchemaConverter.fromAvroSchema(avroSchema);
        for (RSField field : schema.getFields()) {
            assertEquals(genSchema.getField(field.getName()).getBaseType(), field.getBaseType());
        }
    }

    @Test
    public void base32() {
        String fieldStr = "t*t_";
        String underlinePrefixBase32Str = SchemaConverter.base32Encode(fieldStr);
        assertTrue(underlinePrefixBase32Str.startsWith("_"));
        String str = SchemaConverter.base32Decode(underlinePrefixBase32Str);
        assertEquals(str, fieldStr);
    }
}
