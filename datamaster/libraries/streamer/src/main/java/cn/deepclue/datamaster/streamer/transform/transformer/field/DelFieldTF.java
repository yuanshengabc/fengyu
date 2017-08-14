package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.exception.TransformerException;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by xuzb on 14/04/2017.
 */
@TFDef(semaName = "删除列", summary = "删除列 ${sourceFieldName}", order = 1)
public class DelFieldTF extends FieldTransformerTF {

    @SemaDef(semaName = "源列", type = "column")
    private String sourceFieldName;

    private int fieldIndex;

    public DelFieldTF(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    @Override
    protected void prepareSchemaCopy(RSSchema schemaCopy) {
        fieldIndex = findFieldIndex(schemaCopy, sourceFieldName);

        if (!schemaCopy.deleteField(fieldIndex)) {
            throw new TransformerException("Unknown source field name: " + sourceFieldName, "未知源列：" + sourceFieldName);
        }

    }

    @Override
    protected void transformRecordCopy(Record recordCopy) {
        recordCopy.deleteValue(fieldIndex);
    }

    @Override
    protected boolean checkValid(RSSchema schema) {
        return schema.getField(sourceFieldName) != null;
    }

    @Override
    protected String getErrorMsg(RSSchema schema) {
        return "Field '" + sourceFieldName + "' not exists.";
    }

    @Override
    protected String getLocalizedErrorMsg(RSSchema schema) {
        return "字段'" + sourceFieldName + "'不存在.";
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }
}
