package cn.deepclue.datamaster.model;

import cn.deepclue.datamaster.model.schema.RSSchema;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuzb on 14/03/2017.
 */
public class Record {
    private String key; // Identifier for this record.
    private RSSchema schema;
    private List<Object> values = new ArrayList<>();

    public Record(RSSchema schema) {
        this.schema = schema;
    }

    public RSSchema getSchema() {
        return schema;
    }

    public void setSchema(RSSchema schema) {
        this.schema = schema;
    }

    public void addValue(Object value) {
        values.add(value);
    }

    public Object getValue(int index) {
        return values.get(index);
    }

    public Object getValue(String fieldName) {
        return values.get(schema.getFieldIndex(fieldName));
    }

    public Map<String, Object> getValueMap() {
        Map<String, Object> valueMap = new HashMap<>();
        for (int i = 0; i < schema.count(); ++i) {
            valueMap.put(schema.getField(i).getName(), values.get(i));
        }

        return valueMap;
    }

    public List<Object> getValues() {
        return values;
    }

    public int size() {
        return values == null ? 0 : values.size();
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public void setValue(int index, Object value) {
        this.values.set(index, value);
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Transient
    public String getKey() {
        return key;
    }

    public static Record deepCopy(Record record) {
        Record recordCopy = new Record(record.getSchema());
        recordCopy.values.addAll(record.values);
        recordCopy.key = record.key;

        return recordCopy;
    }

    public boolean deleteValue(int fieldIndex) {
        if (fieldIndex >= values.size()) {
            return false;
        }

        values.remove(fieldIndex);
        return true;
    }

    @Override
    public String toString() {
        return "key: " + key + " size: " + values.size();
    }

    public void addValues(List<Object> values) {
        this.values.addAll(values);
    }
}
