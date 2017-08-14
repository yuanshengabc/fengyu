package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by xuzb on 31/03/2017.
 */
@TFDef(semaName = "转换成小写", summary = "列 ${sourceFieldName} 设置小写", order = 1)
public class ToLowerTF extends SingleTextFieldTF {
    public ToLowerTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        return value.toLowerCase();
    }
}
