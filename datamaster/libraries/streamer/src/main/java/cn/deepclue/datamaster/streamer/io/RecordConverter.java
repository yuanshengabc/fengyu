package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.exception.IllegalTransformerException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.util.Date;

/**
 * Created by xuzb on 29/03/2017.
 */
public class RecordConverter {

    private RecordConverter() {
    }

    public static GenericRecord toAvroRecord(Record record, RSSchema rsSchema, Schema avroSchema) {
        GenericRecord avroRecord = new GenericData.Record(avroSchema);

        for (int i = 0; i < rsSchema.count(); ++i) {
            Object value = record.getValue(i);

            RSField field = rsSchema.getField(i);

            value = toAvroRecordValue(value, field);

            avroRecord.put(i, value);
        }

        return avroRecord;
    }

    private static Object toAvroRecordValue(Object value, RSField field) {
        if (value == null) {
            return null;
        }

        checkTypeConsistent(value, field);

        Object newValue = value;
        if (field.getBaseType() == BaseType.DATE) {
            newValue = ((Date) value).getTime();
        }

        return newValue;
    }

    private static Object fromAvroRecordValue(Object value, RSField field) {
        if (value == null) {
            return null;
        }

        Object newValue = value;
        if (field.getBaseType() == BaseType.DATE) {
            newValue = new Date((Long) value);
        } else if (field.getBaseType() == BaseType.TEXT) {
            newValue = value.toString();
        }

        checkTypeConsistent(newValue, field);

        return newValue;
    }

    private static void checkTypeConsistent(Object value, RSField field) {
        switch (field.getBaseType()) {
            case INT:
                assert value instanceof Integer;
                break;
            case LONG:
                assert value instanceof Long;
                break;
            case FLOAT:
                assert value instanceof Float;
                break;
            case DOUBLE:
                assert value instanceof Double;
                break;
            case DATE:
                assert value instanceof Date;
                break;
            case TEXT:
                String valText = (String) value;
                assert valText != null;
                break;

            default:
                throw new IllegalStateException("Unprocessed field base type: " + field.getBaseType());
        }
    }

    public static Record fromAvroRecord(GenericRecord genericRecord, RSSchema rsSchema) {
        Record record = new Record(rsSchema);
        for (int i = 0; i < rsSchema.count(); ++i) {
            RSField field = rsSchema.getField(i);

            Object value = genericRecord.get(i);

            value = fromAvroRecordValue(value, field);

            record.addValue(value);
        }

        return record;
    }

    public static Object toBaseTypeValue(BaseType baseType, String value) {
        if (value == null || "".equals(value)) {
            return null;
        }

        switch (baseType) {
            case TEXT:
                return value;
            case INT:
                return Integer.parseInt(value);
            case LONG:
                return Long.parseLong(value);
            case FLOAT:
                return Float.parseFloat(value);
            case DOUBLE:
                return Double.parseDouble(value);
            case DATE:
                Long valLong = Long.parseLong(value);
                return new Date(valLong);

            default:
                throw new IllegalTransformerException("Failed to convert value to base type.", "转换为基础类型失败。");
        }
    }

    public static Record toESRecord(Record record) {
        Record esRecord = Record.deepCopy(record);
        esRecord.setSchema(SchemaConverter.toESSchema(esRecord.getSchema()));

        return esRecord;
    }
}
