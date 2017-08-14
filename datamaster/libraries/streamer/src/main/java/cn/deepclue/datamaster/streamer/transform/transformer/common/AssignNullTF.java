package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;

/**
 * Created by xuzb on 21/04/2017.
 */
@TFDef(semaName = "字段置空", summary = "列 ${sourceFieldName} 字段置空", order = 1)
public class AssignNullTF extends SingleFieldTF {
    public AssignNullTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected Object transformNotNullValue(RSField field, Object value) {
        return null;
    }
}
