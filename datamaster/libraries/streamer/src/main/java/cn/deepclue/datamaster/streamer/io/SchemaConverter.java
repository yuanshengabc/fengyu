package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.commons.codec.binary.Base32;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xuzb on 29/03/2017.
 */
public class SchemaConverter {

    private SchemaConverter() {
    }

    public static Schema toAvroSchemaByBuilder(RSSchema schema) {
        SchemaBuilder.FieldAssembler<Schema> fieldAssembler = SchemaBuilder.record(base32Encode(schema.getName())).fields();
        for (RSField field : schema.getFields()) {
            SchemaBuilder.FieldBuilder<Schema> fieldBuilder = fieldAssembler.name(base32Encode(field.getName()));
            switch (field.getBaseType()) {
                case INT:
                    fieldBuilder.type().nullable().intType().noDefault();
                    break;

                case LONG:
                    fieldBuilder.type().nullable().longType().noDefault();
                    break;
                case FLOAT:
                    fieldBuilder.type().nullable().floatType().noDefault();
                    break;
                case DOUBLE:
                    fieldBuilder.type().nullable().doubleType().noDefault();
                    break;
                case TEXT:
                    fieldBuilder.type().nullable().stringType().noDefault();
                    break;
                case DATE:
                    fieldBuilder.type().nullable().longBuilder().prop("date", "1").endLong().noDefault();
                    break;

                default:
                    throw new IllegalStateException("Unprocessed field base type: " + field.getBaseType());
            }
        }

        return fieldAssembler.endRecord();
    }

    public static Schema toAvroSchemaFromJson(String json) {
        Schema.Parser parser = new Schema.Parser();
        parser.setValidate(false);
        return parser.parse(json);
    }

    public static Schema toAvroSchemaByJson(RSSchema schema) {
        JSONObject jSchemaObj = new JSONObject();
        jSchemaObj.put("type", "record");
        jSchemaObj.put("name", base32Encode(schema.getName()));
        JSONArray jFieldArr = new JSONArray();
        jSchemaObj.put("fields", jFieldArr);
        for (RSField field : schema.getFields()) {
            JSONObject jObj = new JSONObject();
            jObj.put("name", base32Encode(field.getName()));
            JSONArray jArr = new JSONArray();
            jObj.put("type", jArr);
            switch (field.getBaseType()) {
                case INT:
                    jArr.put("int");
                    break;
                case LONG:
                    jArr.put("long");
                    break;
                case FLOAT:
                    jArr.put("float");
                    break;
                case DOUBLE:
                    jArr.put("double");
                    break;
                case TEXT:
                    jArr.put("string");
                    break;
                case DATE:
                    JSONObject jDataObj = new JSONObject();
                    jDataObj.put("type", "long");
                    jDataObj.put("date", 1);
                    jArr.put(jDataObj);
                    break;

                default:
                    throw new IllegalStateException("Unprocessed field base type: " + field.getBaseType());
            }
            jArr.put("null");
            jFieldArr.put(jObj);
        }

        return toAvroSchemaFromJson(jSchemaObj.toString());
    }


    private static BaseType getBaseType(Schema type) {
        BaseType baseType;
        switch (type.getType()) {
            case BOOLEAN:
            case INT:
                baseType = BaseType.INT;
                break;
            case LONG:
                String dateProp = type.getProp("date");
                if (dateProp != null && "1".equals(dateProp)) {
                    baseType = BaseType.DATE;
                } else {
                    baseType = BaseType.LONG;
                }
                break;
            case FLOAT:
                baseType = BaseType.FLOAT;
                break;
            case DOUBLE:
                baseType = BaseType.DOUBLE;
                break;
            case STRING:
                baseType = BaseType.TEXT;
                break;

            default:
                throw new IllegalStateException("Unexpected avro base type: " + type.getName());
        }

        return baseType;
    }

    public static RSSchema fromAvroSchema(Schema schema) {
        RSSchema rsSchema = new RSSchema();
        rsSchema.setName(base32Decode(schema.getName()));

        for (Schema.Field field : schema.getFields()) {
            List<Schema> types = field.schema().getTypes();
            BaseType baseType = null;
            for (Schema type : types) {
                if (type.getType() != Schema.Type.NULL) {
                    baseType = getBaseType(type);
                }
            }

            if (baseType == null) {
                throw new IllegalStateException("Invalid avro schema: " + schema.getName());
            }

            RSField rsField = new RSField();
            rsField.setName(base32Decode(field.name()));
            rsField.setBaseType(baseType);

            rsSchema.addField(rsField);
        }

        return rsSchema;
    }

    public static String base32Encode(String fieldStr) {
        Base32 base32 = new Base32();
        String base32Str = base32.encodeAsString(fieldStr.getBytes());
        String underlinePrefixBase32Str = "_" + base32Str;
        String paddingZeroStr = underlinePrefixBase32Str.replace("=", "0");
        return paddingZeroStr;
    }

    public static String base32Decode(String paddingZeroStr) {
        String underlinePrefixBase32Str = paddingZeroStr.replace("0", "=");
        String base32Str = underlinePrefixBase32Str.substring(1, underlinePrefixBase32Str.length());
        Base32 base32 = new Base32();
        String fieldStr = new String(base32.decode(base32Str.getBytes()));
        return fieldStr;
    }

    public static String toESSchemaField(String schemaField) {
        String esSchemaField = new Base32().encodeAsString(schemaField.getBytes());
        return esSchemaField.replace("=", "0").toLowerCase();
    }

    public static RSSchema toESSchema(RSSchema schema) {
        RSSchema esSchema = RSSchema.deepCopy(schema);
        for (RSField rsField : esSchema.getFields()) {
            rsField.setName(toESSchemaField(rsField.getName()));
        }

        return esSchema;
    }
}
