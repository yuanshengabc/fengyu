package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by magneto on 17-4-15.
 */

@TFDef(semaName = "增加后缀", summary = "列 ${sourceFieldName} 增加后缀", order = 1)
public class AddSuffixTF extends SingleTextFieldTF {
    
    @SemaDef(semaName = "后缀", type = "text")
    private String suffix;

    public AddSuffixTF(String sourceFieldName, String suffix) {
        super(sourceFieldName);
        this.suffix = suffix;
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        return value + suffix;
    }
}
