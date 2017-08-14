package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by magneto on 17-4-17.
 */
@TFDef(semaName = "保留字母", summary = "列 ${sourceFieldName} 保留字母", order = 1)
public class RetainAlphabetTF extends SingleTextFieldTF {
    public RetainAlphabetTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        if (value.equalsIgnoreCase("null")) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i=0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (isLetter(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }
}
