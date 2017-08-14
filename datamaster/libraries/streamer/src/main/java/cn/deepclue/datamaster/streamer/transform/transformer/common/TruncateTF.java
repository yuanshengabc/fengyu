package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by magneto on 17-4-17.
 */
@TFDef(semaName = "截取n位字符", summary = "列 ${sourceFieldName} 截断字段", order = 1)
public class TruncateTF extends SingleTextFieldTF {
    @SemaDef(semaName = "位数", type = "number", minValue = 1)
    private int n;

    @SemaDef(semaName = "", type = "boolean", domain = "{true: \"截取前n位\", false: \"截取后n位\"}")
    private boolean head;

    public TruncateTF(String sourceFieldName, int n, boolean head) {
        super(sourceFieldName);
        this.n = n;
        this.head = head;
    }

    @Override
    protected String transformTextValue(RSField field, String value) {
        int l = value.length();
        if (n >= l) {
            return value;
        }

        if (head) {
            return value.substring(0, n);
        }

        return value.substring(l - n, l);
    }
}
