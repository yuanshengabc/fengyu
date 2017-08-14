package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;

/**
 * Created by xuzb on 20/04/2017.
 */
public abstract class SingleTextFieldTF extends SingleFieldTF {
    public SingleTextFieldTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected Object transformNotNullValue(RSField field, Object value) {
        return transformTextValue(field, value.toString());
    }

    protected abstract String transformTextValue(RSField field, String value);

    @Override
    protected boolean checkFieldValid(RSField field) {
        return field.getBaseType() == BaseType.TEXT;

    }

    @Override
    protected String getFieldErrorMsg(RSField field) {
        return "only support type TEXT for now.";
    }

    @Override
    protected String getFieldLocalizedErrorMsg(RSField field) {
        return "暂时只支持TEXT类型";
    }
}
