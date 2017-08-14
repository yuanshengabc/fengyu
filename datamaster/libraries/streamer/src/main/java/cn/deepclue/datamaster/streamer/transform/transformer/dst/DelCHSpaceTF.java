package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleTextFieldTF;

/**
 * Created by yuansheng on 17-6-19.
 */
public class DelCHSpaceTF extends SingleTextFieldTF {
    public DelCHSpaceTF(String sourceFieldName){
        super(sourceFieldName);
    }


    @Override
    protected String transformTextValue(RSField field, String value) {
        String trimmedValue = TransformHelper.trim(value);
        String clean;

        //有中文时，去掉全部空格
        if (hasChinese(trimmedValue)){
            clean = TransformHelper.trimAll(trimmedValue);
        }
        //没有中文时，多个相连空格保留一个
        else {
            clean = delSpace(trimmedValue);
        }

        return clean;
    }

    //判断字符串是否包含中文
    private boolean hasChinese(String value){
        boolean result = false;
        for (int i = 0; i < value.length(); ++i) {
            //中文区间\u4e00~\u9fa5
            if (value.charAt(i) >= '\u4e00' && value.charAt(i) <= '\u9fa5') {
                result = true;
                break;
            }
        }
        return result;
    }

    private String delSpace(String value) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < value.length(); ++i) {
            if (value.charAt(i) == ' ') {
                if (value.charAt(i - 1) != ' ') {
                    str.append(value.charAt(i));
                }
            } else {
                str.append(value.charAt(i));
            }
        }
        return str.toString();
    }
}