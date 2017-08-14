package cn.deepclue.datamaster.streamer.transform.transformer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.exception.IllegalTransformerException;
import cn.deepclue.datamaster.streamer.transform.SemaDef;

/**
 * Created by xuzb on 30/03/2017.
 */
public abstract class SingleFieldTF extends FilterTransformer {

    @SemaDef(semaName = "目标字段", type = "column", domain = "source")
    private String sourceFieldName;

    private int fieldIndex = -1;
    private RSField targetField;

    public SingleFieldTF(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    @Override
    protected RSSchema prepareSchemaTF(RSSchema schema) {
        fieldIndex = findFieldIndex(schema, sourceFieldName);
        targetField = findField(schema, sourceFieldName);

        if (!checkFieldValid(targetField)) {
            throw new IllegalTransformerException(getFieldErrorMsg(targetField), getFieldLocalizedErrorMsg(targetField));
        }

        return schema;
    }

    @Override
    protected Record transformAccepted(Record record) {
        Object value = record.getValue(fieldIndex);
        Object newValue;
        if (value != null) {
            newValue = transformNotNullValue(targetField, value);
        } else {
            newValue = transformNullValue(targetField);
        }
        if (newValue == value) {
            return record;
        }

        Record newRecord = Record.deepCopy(record);
        newRecord.setValue(fieldIndex, newValue);

        return newRecord;
    }

    protected Object transformNullValue(RSField targetField) {
        return null;
    }

    protected abstract Object transformNotNullValue(RSField field, Object value);

    public String getSourceFieldName() {
        return sourceFieldName;
    }


    @Override
    protected boolean checkValid(RSSchema schema) {
        return schema.getField(sourceFieldName) != null;
    }

    @Override
    protected String getErrorMsg(RSSchema schema) {
        return "Source Field '" + sourceFieldName + "' not exists.";
    }

    @Override
    protected String getLocalizedErrorMsg(RSSchema schema) {
        return "源列'" + sourceFieldName + "'不存在。";
    }

    protected boolean checkFieldValid(RSField field) {
        return true;
    }

    protected String getFieldErrorMsg(RSField field) {
        return "Unhandled single field tranform rule exception.";
    }

    protected String getFieldLocalizedErrorMsg(RSField field) {
        return "未处理的单字段规则异常.";
    }
}
