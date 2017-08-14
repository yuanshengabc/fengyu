package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.RegexFilterTF;

/**
 * Created by xuzb on 19/04/2017.
 */
@TFDef(semaName = "电子邮箱清洗", summary = "列 ${sourceFieldName} 电子邮箱清洗", order = 1)
public class EmailTF extends RegexFilterTF {

    public EmailTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String regex() {
        return "^.*@.*$";
    }

    @Override
    protected String preTransformNotNullValue(String value) {
        String trimmedValue = TransformHelper.trimAll(value);
        int lastDotIndex = trimmedValue.length();
        while (lastDotIndex > 0 && trimmedValue.charAt(lastDotIndex - 1) == '.') {
            --lastDotIndex;
        }
        String trimTailDotsValue = trimmedValue.substring(0, lastDotIndex);
        return trimTailDotsValue.toLowerCase();
    }

    @Override
    protected String transformCandidateValue(String candidateValue) {
        int sepIndex = candidateValue.lastIndexOf('@');

        // Split username and domain
        String username = candidateValue.substring(0, sepIndex);
        String domain = candidateValue.substring(sepIndex + 1, candidateValue.length());

        // Append username to string buffer
        StringBuilder sb = new StringBuilder(username);
        sb.append('@');

        if (domain.length() == 0) {
            return sb.toString();
        }
        // Trim starting dots 'abc@.163.com' -> 'abc@163.com'
        int i = 0;
        for (; i < domain.length(); ++i) {
            if (domain.charAt(i) != '.') {
                break;
            }
        }

        // Remove multi dots like '163..com' -> '163.com'
        for (; i < domain.length() - 1; ++i) {
            char c = domain.charAt(i);
            char nextc = domain.charAt(i + 1);
            if (c == '.' && nextc == '.') {
                continue;
            }

            sb.append(c);
        }

        // Append last char if it does not end with '.'
        char lastChar = domain.charAt(domain.length() - 1);
        if (lastChar != '.') {
            sb.append(lastChar);
        }

        return sb.toString();
    }
}
