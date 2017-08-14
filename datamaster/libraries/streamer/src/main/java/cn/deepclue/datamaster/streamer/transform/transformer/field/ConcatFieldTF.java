package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

/**
 * Created by xuzb on 14/04/2017.
 */
@TFDef(semaName = "拼接列", summary = "拼接列 ${leftFieldName} 和列 ${rightFieldName}", order = 1)
public class ConcatFieldTF extends InsertFieldTransformerTF {

    @SemaDef(semaName = "源列", type = "column", domain = "source")
    private String leftFieldName;

    @SemaDef(semaName = "拼接列", type = "column", domain = "target")
    private String rightFieldName;

    private String targetFieldName;

    @SemaDef(semaName = "分隔符", type = "text")
    private String sep = "-";

    private int leftFieldIndex;
    private int rightFieldIndex;

    public ConcatFieldTF(String leftFieldName, String rightFieldName, String targetFieldName, String sep) {
        this.leftFieldName = leftFieldName;
        this.rightFieldName = rightFieldName;

        // The default target field name is lf_rf.
        if (targetFieldName == null || "".equals(targetFieldName)) {
            this.targetFieldName = leftFieldName + "_" + rightFieldName;
        } else {
            this.targetFieldName = targetFieldName;
        }

        this.sep = sep;
    }

    @Override
    protected boolean checkValid(RSSchema schema) {
        return schema.getField(leftFieldName) != null && schema.getField(rightFieldName) != null;

    }

    @Override
    protected String getErrorMsg(RSSchema schema) {
        return leftFieldName + " or " + rightFieldName + " field not exists.";
    }

    @Override
    protected String getLocalizedErrorMsg(RSSchema schema) {
        return leftFieldName + "或者" + rightFieldName + "字段不存在.";
    }

    @Override
    protected int computeInsertPosition(RSSchema schema) {
        return findFieldIndex(schema, leftFieldName);
    }

    @Override
    protected RSField createField(RSSchema schema) {
        leftFieldIndex = findFieldIndex(schema, leftFieldName);
        rightFieldIndex = findFieldIndex(schema, rightFieldName);

        if (targetFieldName == null || "".equals(targetFieldName)) {
            targetFieldName = leftFieldName + "_" + rightFieldName;
        }

        return new RSField(targetFieldName, BaseType.TEXT);
    }

    @Override
    protected Object createValue(Record record) {
        Object leftValue = record.getValue(leftFieldIndex);
        Object rightValue = record.getValue(rightFieldIndex);
        if (leftValue == null && rightValue == null) {
            return null;
        } else if (leftValue == null) {
            return rightValue;
        } else if (rightValue == null) {
            return leftValue;
        } else {
            return leftValue.toString() + sep + rightValue;
        }
    }

    public String getLeftFieldName() {
        return leftFieldName;
    }

    public String getRightFieldName() {
        return rightFieldName;
    }

    @Override
    public boolean isCascadedWith(Transformer followingTransformer) {
        return TransformHelper.isCascadedWith(targetFieldName, followingTransformer);
    }
}
