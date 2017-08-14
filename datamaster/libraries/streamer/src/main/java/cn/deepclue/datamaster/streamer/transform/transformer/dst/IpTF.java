package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.RegexFilterTF;

/**
 * Created by magneto on 17-4-17.
 */
@TFDef(semaName = "IP地址清洗", summary = "列 ${sourceFieldName} 清洗IP地址", order = 1)
public class IpTF extends RegexFilterTF {

    public IpTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String regex() {
        return "^((0*1?\\d?\\d|0*2[0-4]\\d|0*25[0-5])\\.){2,3}(0*1?\\d?\\d|0*2[0-4]\\d|0*25[0-5])$";
    }

    @Override
    protected String preTransformNotNullValue(String value) {
        //trim "."
        String trimmedValue = TransformHelper.trimAll(value);
        int lastDotIndex = trimmedValue.length();
        while (lastDotIndex > 0 && trimmedValue.charAt(lastDotIndex - 1) == '.') {
            --lastDotIndex;
        }
        int firstDotIndex = 0;
        while (firstDotIndex < lastDotIndex && trimmedValue.charAt(firstDotIndex) == '.') {
            ++firstDotIndex;
        }
        String trimedDotsValue = trimmedValue.substring(firstDotIndex, lastDotIndex);
        if (trimedDotsValue.length() == 0) {
            return trimedDotsValue;
        }
        //Remove multi dots like '10..10' -> '10.10'
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trimedDotsValue.length() - 1; ++i) {
            char c = trimedDotsValue.charAt(i);
            char nextc = trimedDotsValue.charAt(i + 1);
            if (c == '.' && nextc == '.') {
                continue;
            }

            sb.append(c);
        }
        // Append last char if it does not end with '.'
        char lastChar = trimedDotsValue.charAt(trimedDotsValue.length() - 1);
        if (lastChar != '.') {
            sb.append(lastChar);
        }

        return sb.toString();
    }

    @Override
    protected String transformCandidateValue(String candidateValue) {
        StringBuilder sb = new StringBuilder();
        String[] array = candidateValue.split("\\.");
        for (String item : array) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(Integer.valueOf(item));
        }

        return sb.toString();
    }
}
