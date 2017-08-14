package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleTextFieldTF;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by magneto on 17-4-17.
 */
@TFDef(semaName = "统一性别格式", summary = "列 ${sourceFieldName} 统一性别格式", order = 1)
public class UnifyGenderTF extends SingleTextFieldTF {
    private static Set<String> males = new HashSet<>(Arrays.asList("m", "male", "mr", "先生", "男士", "男"));
    private static Set<String> females = new HashSet<>(Arrays.asList("f", "female", "ms", "miss", "mrs", "mstr",
                                         "madam", "mdm", "女士", "小姐", "太太", "夫人", "女"));

    public UnifyGenderTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
            String str = value;
            str = TransformHelper.trimAll(str).toLowerCase();
            return males.contains(str)?"男":(females.contains(str))?"女":value;
    }
}
