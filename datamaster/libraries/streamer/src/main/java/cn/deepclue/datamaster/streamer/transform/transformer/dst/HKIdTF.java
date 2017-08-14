package cn.deepclue.datamaster.streamer.transform.transformer.dst;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.RegexFilterTF;

import java.util.*;

@TFDef(semaName = "HK身份证号清洗", summary = "列 ${sourceFieldName} 清洗HK身份证号", order = 1)
public class HKIdTF extends RegexFilterTF {

    public HKIdTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    private static final char[] brastr = {'(', '[', '{', ')', ']', '}'};

    @Override
    //正则：1～2位字母+6位数字+0～1个正括号+0～2个数字或A或a+0～1个反括号
    protected String regex() {
        return "^[a-zA-Z]{1,2}[0-9]{6}[\\(|\\[|\\{]{0,1}[0-9Aa]{0,2}[\\)|\\]|\\}]{0,1}$";
    }

    @Override
    protected String preTransformNotNullValue(String value) {
        String trimmedValue = TransformHelper.trimAll(value);
        //截取身份证中的数字
        String numChar = TransformHelper.getNum(trimmedValue,6);
        //判断是否为假身份证号
        if (TransformHelper.isFakeSeq(numChar,1)) {
            trimmedValue = "";
        }
        return trimmedValue;
    }

    @Override
    protected String transformCandidateValue(String value) {
        StringBuilder nobracket = new StringBuilder();
        String clean;
        //去括号并用nobracket保存
        for (int i = 0; i < value.length(); ++i) {
            if (!(TransformHelper.contain(brastr,value.charAt(i)))) {
                nobracket.append(value.charAt(i));
            }
        }
        //小写转大写
        nobracket = new StringBuilder(nobracket.toString().toUpperCase());
        if (nobracket.length() == 7) { //7位时，A135790
            clean = nobracket.toString();
        } else if (nobracket.length() == 8) {
            if (Character.isUpperCase(nobracket.charAt(1))) { //8位且第二位为字母时，AB145369
                clean = nobracket.toString();
            } else { //8位且第二位为数字时，A714536(9)
                clean = (nobracket.substring(0,7) + '(' + nobracket.charAt(7) + ')');
            }
        } else if (nobracket.length() == 9) {
            if (Character.isUpperCase(nobracket.charAt(1))) { //9位且第二位为字母时，AB145369(8)
                clean = (nobracket.substring(0,8) + '(' + nobracket.charAt(8) + ')');
            } else { //9位且第二位为数字时，A571453(69)
                clean = (nobracket.substring(0,7) + '(' + nobracket.substring(7,9) + ')');
            }
        } else { //10位时，CD756321(45)
            clean = (nobracket.substring(0,8) + '(' + nobracket.substring(8,10) + ')');
        }
        return clean;
    }

}

