package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleTextFieldTF;

/**
 * Created by yuansheng on 6/20/17.
 */
@TFDef(semaName = "邮政编码清洗",summary = "列 ${sourceFieldName} 清洗邮政编码", order = 1)
public class TWZipCodeTF extends SingleTextFieldTF {
    public TWZipCodeTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        String trimmedValue = TransformHelper.trimAll(value);
        //长度大于6或小于2置空
        if (trimmedValue.length() > 6 || trimmedValue.length() < 2) {
            trimmedValue = "";
        }//假邮编如123456,987654,111111置空
        if (TransformHelper.isFakeSeq(value,1)) {
            trimmedValue = "";
        }
        return trimmedValue;
    }
}
