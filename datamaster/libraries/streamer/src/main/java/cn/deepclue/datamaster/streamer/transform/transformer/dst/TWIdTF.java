package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.RegexFilterTF;

/**
 * Created by yuansheng on 6/20/17.
 */
@TFDef(semaName = "台湾身份证号清洗", summary = "列 ${sourceFieldName} 清洗台湾身份证号", order = 1)
public class TWIdTF extends RegexFilterTF {
    public TWIdTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String regex() {
        return ("^[a-zA-Z]{1,2}[0-9]{8,9}$");
    }

    @Override
    protected String preTransformNotNullValue(String value) {
        String trimmedValue = TransformHelper.trimAll(value);
        //截取前八位数字
        String numChars = TransformHelper.getNum(trimmedValue,8);
        //假身份证号如12345678,98765432,11112222置空
        if (TransformHelper.isFakeSeq(numChars,1)) {
            trimmedValue = "";
        }
        return trimmedValue;
    }

    @Override
    protected String transformCandidateValue(String value) {
        //小写转大写
        String UpperValue = value.toUpperCase();
        //大于10位置空
        if (UpperValue.length() > 10) {
            UpperValue = "";
        }
        return UpperValue;
    }
}
