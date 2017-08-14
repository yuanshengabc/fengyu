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
@TFDef(semaName = "统一婚姻状态格式", summary = "列 ${sourceFieldName} 统一婚姻状态格式", order = 1)
public class UnifyMarrigeTF extends SingleTextFieldTF {
    private static Set<String> marrieds = new HashSet<String>(Arrays.asList("married", "marry", "notsingle", "mrs",
            "已婚", "是"));
    private static Set<String> unmarrieds = new HashSet<String>(Arrays.asList("single", "unmarry", "unmarried", "miss",
            "未婚", "否"));
    private static Set<String> divorce = new HashSet<String>(Arrays.asList("divorce", "divorced", "divorcer", "离异"));
    private static Set<String> widow = new HashSet<String>(Arrays.asList("widow", "widower", "丧偶"));

    public UnifyMarrigeTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        String str = value;
        str = TransformHelper.trimAll(str).toLowerCase();
        if (marrieds.contains(str)) {
            return "已婚";
        }
        if (unmarrieds.contains(str)) {
            return "未婚";
        }
        if (divorce.contains(str)) {
            return "离异";
        }
        if (widow.contains(str)) {
            return "丧偶";
        }
        return value;
    }
}
