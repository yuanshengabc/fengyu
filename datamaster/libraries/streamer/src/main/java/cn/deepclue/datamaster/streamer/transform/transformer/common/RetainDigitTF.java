package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by magneto on 17-4-17.
 */
@TFDef(semaName = "保留数字", summary = "列 ${sourceFieldName} 保留数字", order = 1)
public class RetainDigitTF extends SingleTextFieldTF {
    public RetainDigitTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            int cp = value.codePointAt(i);
            if (Character.isDigit(cp)) {
                sb.append((char) cp);
            }
        }

        return sb.toString();
    }
}
