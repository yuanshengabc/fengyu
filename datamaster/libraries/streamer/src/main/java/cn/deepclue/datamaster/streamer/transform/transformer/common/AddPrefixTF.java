package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by magneto on 17-4-15.
 */
@TFDef(semaName = "增加前缀", summary = "列 ${sourceFieldName} 增加前缀", order = 1)
public class AddPrefixTF extends SingleTextFieldTF {
    @SemaDef(semaName = "前缀", type = "text")
    private String prefix;

    public AddPrefixTF(String sourceFieldName, String prefix) {
        super(sourceFieldName);
        this.prefix = prefix;
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        return prefix + value;
    }
}
