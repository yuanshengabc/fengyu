package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleTextFieldTF;

/**
 * Created by yuansheng on 6/20/17.
 */
@TFDef(semaName = "台湾电话号码清洗",summary = "列 ${sourceFieldName} 清洗台湾电话号码", order = 1)
public class  TWTelTF extends SingleTextFieldTF {
    public TWTelTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    private static final char[] telChars = {'0','1','2','3','4','5','6','7','8','9'};
    @Override
    protected String transformTextValue(RSField field, String value) {
        //去空格
        String trimmedValue = TransformHelper.trimAll(value);
        //用clean保存返回值
        String clean;
        //提取数字并去分机号
        StringBuilder telstr = new StringBuilder();
        for (int i = 0; i < trimmedValue.length(); ++i) {
            if (trimmedValue.charAt(i) == '#') {
                break;
            }
            char c = trimmedValue.charAt(i);
            if (TransformHelper.contain(telChars,c)) {
                telstr.append(c);
            }
        }
        clean = telstr.toString();
        //取前八位数字用来判断是否是假号码
        String numChars = TransformHelper.getNum(clean,8);
        //位数小于5时返回clean
        if (clean.length() < 6) {
            return clean;
        }
        //假号码如12345678,11112222置空
        if (TransformHelper.isFakeSeq(numChars,2)) {
            clean = "";
        }
        //8~10位号码加前缀886
        if (clean.length() > 7 && clean.length() < 11 ) {
            clean = "886" + clean;
        }
        return clean;
    }
}
