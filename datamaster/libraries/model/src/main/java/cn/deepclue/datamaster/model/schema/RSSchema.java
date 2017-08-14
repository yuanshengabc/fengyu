package cn.deepclue.datamaster.model.schema;

import cn.deepclue.datamaster.model.exception.SchemaException;

import java.util.*;

/**
 * Created by xuzb on 15/03/2017.
 */
public class RSSchema {
    private String name;
    private List<RSField> fields = new ArrayList<>();
    private Map<String, RSField> fieldMap = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RSField> getFields() {
        return fields;
    }

    public void setFields(List<RSField> fields) {
        for (RSField field : fields) {
            addField(field);
        }
    }

    public void addField(RSField field) {
        fields.add(field);
        if (fieldMap.put(field.getName(), field) != null) {
            throw new SchemaException("Field '" + field.getName() + "' exists.",
                    "列'" + field.getName() + "'已存在.");
        }
    }

    public int getFieldIndex(String fieldName) {
        for (int i = 0; i < count(); ++i) {
            RSField field = getField(i);
            if (field.getName().equals(fieldName)) {
                return i;
            }
        }

        return -1;
    }

    public RSField getField(String fieldName) {
        return fieldMap.get(fieldName);
    }

    public RSField getField(int index) {
        return fields.get(index);
    }

    public int count() {
        return fields.size();
    }

    public static RSSchema deepCopy(RSSchema schema) {
        RSSchema newSchema = new RSSchema();
        newSchema.setName(schema.name);
        for (RSField field : schema.getFields()) {
            newSchema.addField(RSField.deepCopy(field));
        }

        return newSchema;
    }

    public boolean deleteField(int fieldIndex) {
        if (fieldIndex >= fields.size()) {
            return false;
        }

        RSField field = fields.remove(fieldIndex);
        if (field == null) {
            return false;
        }

        fieldMap.remove(field.getName());
        return true;
    }

    public boolean deleteField(String fieldName) {
        fieldMap.remove(fieldName);
        Iterator<RSField> iter = fields.iterator();
        while (iter.hasNext()) {
            RSField field = iter.next();
            if (field.getName().equals(fieldName)) {
                iter.remove();
                return true;
            }
        }

        return false;
    }

    public void addFields(List<RSField> fields) {
        this.fields.addAll(fields);
    }
}
