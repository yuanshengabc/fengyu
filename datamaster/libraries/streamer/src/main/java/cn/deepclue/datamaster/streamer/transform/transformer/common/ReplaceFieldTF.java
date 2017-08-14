package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by xuzb on 18/04/2017.
 */
@TFDef(semaName = "字段替换", summary = "列 ${sourceFieldName} 字段替换", order = 1)
public class ReplaceFieldTF extends SingleTextFieldTF {

    @SemaDef(semaName = "赋值为", type = "text")
    private String targetValue;

    public ReplaceFieldTF(String sourceFieldName, String targetValue) {
        super(sourceFieldName);
        this.targetValue = targetValue;
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        return targetValue;
    }

    @Override
    protected Object transformNullValue(RSField targetField) {
        return targetValue;
    }
}
