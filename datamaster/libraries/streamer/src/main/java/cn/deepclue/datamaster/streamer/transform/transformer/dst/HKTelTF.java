package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleTextFieldTF;

/**
 * Created by xuzb on 17/04/2017.
 */
@TFDef(semaName = "HK电话号码清洗", summary = "列 ${sourceFieldName} 清洗HK电话号码", order = 1)
public class HKTelTF extends SingleTextFieldTF {

    private static final char[] telchars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '(', ')'};

    public HKTelTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        String trimmedValue = TransformHelper.trimAll(value);

        String telStr = keepTelchars(trimmedValue);
        String prefix = "";
        if (telStr.startsWith("+852")) {
            prefix = "+852";
            telStr = telStr.substring(4);
        } else if (telStr.startsWith("852")) {
            prefix = "852";
            telStr = telStr.substring(3);
        }

        // Strip Ext Number
        telStr = stripExtNumber(telStr);

        // Fake tel
        if (TransformHelper.isFakeSeq(telStr,2)) {
            return null;
        }

        if ((telStr.length() == 9 && telStr.charAt(0) == '4') || telStr.length() == 8) {
            telStr = "852" + telStr;
        } else {
            telStr = prefix + telStr;
        }

        return telStr;
    }

    private String stripExtNumber(String telStr) {
        if (telStr.isEmpty()) {
            return telStr;
        }

        // 19293329-1029
        // 19293329(1029...
        // +863738273-2823
        StringBuilder sb = new StringBuilder();

        // Max keep length
        int keepLength = 8;
        if (telStr.charAt(0) == '+') {
            keepLength = 10;
        }

        for (int i = 0; i < telStr.length(); ++i) {
            char c = telStr.charAt(i);
            if (c == '+' || c == ')') {
            } else if (c == '(' || c == '-') {
                if (sb.length() >= keepLength) {
                    break;
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }


    private String keepTelchars(String value) {
        // FIXME: Move to StringUtils
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (TransformHelper.contain(telchars,value.charAt(i))) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

}
