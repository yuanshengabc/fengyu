package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xuzb on 14/04/2017.
 */
@TFDef(semaName = "拆分列", summary = "拆分列 ${sourceFieldName} 为若干列", order = 1)
public class SplitFieldTF extends Transformer {

    @SemaDef(semaName = "复制列", type = "column")
    private String sourceFieldName;

    @SemaDef(semaName = "分隔符", type = "text")
    private String sep;

    @SemaDef(semaName = "拆分列数", type = "number", minValue = 2)
    private int splitCount;

    private int fieldIndex;

    private String quotedSep;

    private List<String> targetFieldNames;

    private RSSchema newSchema;

    public SplitFieldTF(String sourceFieldName, int splitCount, String sep) {
        this.sourceFieldName = sourceFieldName;
        this.splitCount = splitCount;
        this.sep = sep;
    }

    @Override
    public RSSchema prepareValidSchema(RSSchema schema) {
        fieldIndex = findFieldIndex(schema, sourceFieldName);
        targetFieldNames = new ArrayList<>();

        newSchema = new RSSchema();
        newSchema.setName(schema.getName());

        int i = 0;
        for (; i <= fieldIndex; ++i) {
            newSchema.addField(RSField.deepCopy(schema.getField(i)));
        }

        addTargetFields(newSchema, schema);

        for (; i < schema.count(); ++i) {
            newSchema.addField(RSField.deepCopy(schema.getField(i)));
        }

        this.quotedSep = Pattern.quote(sep);
        return newSchema;
    }

    private void addTargetFields(RSSchema schema, RSSchema oldSchema) {
        int addCount = 0;
        int index = 1;
        while (addCount < splitCount) {
            String targetFieldName = sourceFieldName + "_" + index;
            index++;
            if (checkFieldExists(oldSchema, targetFieldName)) {
                continue;
            }
            targetFieldNames.add(targetFieldName);
            schema.addField(new RSField(targetFieldName, BaseType.TEXT));
            addCount++;
        }
    }

    @Override
    public Record transform(Record record) {
        Record newRecord = new Record(newSchema);
        newRecord.setKey(record.getKey());

        Object value = record.getValue(fieldIndex);
        String[] targetValues = {};

        if (value != null) {
            String strValue = value.toString();
            targetValues = strValue.split(quotedSep, targetFieldNames.size());
        }

        int i = 0;
        for (; i <= fieldIndex; ++i) {
            newRecord.addValue(record.getValue(i));
        }

        addTargetValues(newRecord, targetValues);

        for (; i < record.size(); ++i) {
            newRecord.addValue(record.getValue(i));
        }

        return newRecord;
    }

    private void addTargetValues(Record record, String[] targetValues) {
        int i = 0;
        for (; i < targetValues.length; ++i) {
            record.addValue(targetValues[i]);
        }

        for (; i < targetFieldNames.size(); ++i) {
            record.addValue(null);
        }
    }

    @Override
    protected boolean checkValid(RSSchema schema) {
        RSField field = schema.getField(sourceFieldName);
        if (field == null || field.getBaseType() != BaseType.TEXT) {
            return false;
        }

        if (targetFieldNames != null && !targetFieldNames.isEmpty()) {
            for (String targetFieldName : targetFieldNames) {
                if (schema.getField(targetFieldName) != null) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected String getErrorMsg(RSSchema schema) {
        return "Source field is not TEXT type/invalid target field '" + sourceFieldName + "' or split field.";
    }

    @Override
    protected String getLocalizedErrorMsg(RSSchema schema) {
        return "源列不为TEXT类型/无效的目标列'" + sourceFieldName + "'或拆分列名.";
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    @Override
    public boolean isCascadedWith(Transformer followingTransformer) {
        for (String targetFieldName : targetFieldNames) {
            if (TransformHelper.isCascadedWith(targetFieldName, followingTransformer)) {
                return true;
            }
        }

        return false;
    }
}
