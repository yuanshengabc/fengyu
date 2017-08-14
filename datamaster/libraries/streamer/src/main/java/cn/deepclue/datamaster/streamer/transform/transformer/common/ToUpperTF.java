package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by xuzb on 13/04/2017.
 */
@TFDef(semaName = "转换成大写", summary = "列 ${sourceFieldName} 设置大写", order = 2)
public class ToUpperTF extends SingleTextFieldTF {
    public ToUpperTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        return value.toUpperCase();
    }
}
