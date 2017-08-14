package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;

import java.util.regex.Pattern;

/**
 * Created by xuzb on 18/04/2017.
 */
@TFDef(semaName = "字符串替换", summary = "列 ${sourceFieldName} 字符串替换", order = 1)
public class ReplaceTF extends SingleTextFieldTF {

    @SemaDef(semaName = "目标", type = "text")
    private String sourceStr;

    @SemaDef(semaName = "替换为", type = "text")
    private String targetStr;

    public ReplaceTF(String sourceFieldName, String sourceStr, String targetStr) {
        super(sourceFieldName);
        this.sourceStr = sourceStr;
        this.targetStr = targetStr;
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        return value.replaceAll(Pattern.quote(sourceStr), targetStr);
    }
}
