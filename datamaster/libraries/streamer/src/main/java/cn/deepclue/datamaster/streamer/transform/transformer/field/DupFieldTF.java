package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

/**
 * Created by xuzb on 14/04/2017.
 */
@TFDef(semaName = "复制列", summary = "复制列 ${sourceFieldName} 为 ${targetFieldName}", order = 1)
public class DupFieldTF extends InsertFieldTransformerTF {

    @SemaDef(semaName = "复制列", type = "column")
    private String sourceFieldName;

    @SemaDef(semaName = "列名", type = "text", description = "默认添加_COPY，并1,2..自增", require = false)
    private String targetFieldName;

    private int fieldIndex;

    public DupFieldTF(String sourceFieldName, String targetFieldName) {
        this.sourceFieldName = sourceFieldName;
        this.targetFieldName = targetFieldName;
    }

    @Override
    protected boolean checkValid(RSSchema schema) {
        return schema.getField(sourceFieldName) != null;
    }

    @Override
    protected String getErrorMsg(RSSchema schema) {
        return "Source field name '" + sourceFieldName + "' not exists.";
    }

    @Override
    protected String getLocalizedErrorMsg(RSSchema schema) {
        return "源列名'" + sourceFieldName + "'不存在";
    }

    @Override
    protected int computeInsertPosition(RSSchema schema) {
        return findFieldIndex(schema, sourceFieldName);
    }

    @Override
    protected RSField createField(RSSchema schema) {
        RSField field = findField(schema, sourceFieldName);
        fieldIndex = findFieldIndex(schema, sourceFieldName);

        if (targetFieldName == null || "".equals(targetFieldName)) {
            targetFieldName = sourceFieldName + "_COPY";
            int i = 0;
            while (checkFieldExists(schema, targetFieldName)) {
                ++i;
                targetFieldName = sourceFieldName + "_COPY" + i;
            }
        }

        RSField newField = RSField.deepCopy(field);
        newField.setName(targetFieldName);
        return newField;
    }

    @Override
    protected Object createValue(Record record) {
        return record.getValue(fieldIndex);
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    @Override
    public boolean isCascadedWith(Transformer followingTransformer) {
        return TransformHelper.isCascadedWith(targetFieldName, followingTransformer);
    }
}
