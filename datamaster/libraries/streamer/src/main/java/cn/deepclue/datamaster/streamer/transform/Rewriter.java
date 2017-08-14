package cn.deepclue.datamaster.streamer.transform;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuzb on 15/03/2017.
 */
public class Rewriter {
    private Map<String, String> fieldMappings;

    private List<Integer> selectedIndices = new ArrayList<>();

    public Rewriter() {
        fieldMappings = new HashMap<>();
    }

    public Rewriter(Map<String, String> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public Map<String, String> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(Map<String, String> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public RSSchema rewriteSchema(RSSchema schema) {
        RSSchema targetSchema = new RSSchema();
        targetSchema.setName(schema.getName());

        selectedIndices.clear();

        for (int i = 0; i < schema.count(); ++i) {
            RSField field = schema.getField(i);
            String targetFieldName = fieldMappings.get(field.getName());
            if (targetFieldName != null && !"".equals(targetFieldName)) {
                RSField newField = new RSField();
                newField.setBaseType(field.getBaseType());
                newField.setName(targetFieldName);

                //  RSSchema can make sure all fields are unique.
                targetSchema.addField(newField);

                // Record selected index for record rewriting.
                selectedIndices.add(i);
            }
        }

        return targetSchema;
    }

    public Record rewriteRecord(RSSchema schema, Record record) {
        Record newRecord = new Record(schema);
        newRecord.setKey(record.getKey());
        for (int i : selectedIndices) {
            newRecord.addValue(record.getValue(i));
        }

        return newRecord;
    }
}
