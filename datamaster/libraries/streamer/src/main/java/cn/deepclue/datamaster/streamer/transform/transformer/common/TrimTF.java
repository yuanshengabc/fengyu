package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;

/**
 * Created by magneto on 17-4-17.
 */
@TFDef(semaName = "去除空格", summary = "列 ${sourceFieldName} 删除空格", order = 1)
public class TrimTF extends SingleTextFieldTF {

    @SemaDef(semaName = "", type = "boolean", domain = "{true: \"去除全部空格\", false: \"去除前后空格\"}")
    private boolean all;

    public TrimTF(String sourceFieldName, boolean all) {
        super(sourceFieldName);
        this.all = all;
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        if (all) {
            return TransformHelper.trimAll(value);
        }
        return TransformHelper.trim(value);
    }
}
