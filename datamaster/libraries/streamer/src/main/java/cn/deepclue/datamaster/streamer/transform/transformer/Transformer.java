package cn.deepclue.datamaster.streamer.transform.transformer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.exception.IllegalTransformerException;

/**
 * Created by xuzb on 15/03/2017.
 */
public abstract class Transformer {
    public RSSchema prepareSchema(RSSchema schema) {
        if (!checkValid(schema)) {
            throw new IllegalTransformerException("Illegal transformer: " + getClass().getSimpleName() + ". " + getErrorMsg(schema),
                    "非法的转换：" + getClass().getSimpleName() + ". " + getLocalizedErrorMsg(schema));
        }

        return prepareValidSchema(schema);
    }

    public abstract RSSchema prepareValidSchema(RSSchema schema);

    public abstract Record transform(Record record);

    protected int findFieldIndex(RSSchema schema, String sourceFieldName) {
        int fieldIndex = schema.getFieldIndex(sourceFieldName);

        if (fieldIndex == -1) {
            throw new IllegalTransformerException("Invalid source field name: " + sourceFieldName, "无效的源列： " + sourceFieldName);
        }

        return fieldIndex;
    }

    protected RSField findField(RSSchema schema, String sourceFieldName) {
        RSField field = schema.getField(sourceFieldName);

        if (field == null) {
            throw new IllegalTransformerException("Invalid source field name: " + sourceFieldName, "无效的源列: " + sourceFieldName);
        }

        return field;
    }

    protected boolean checkFieldExists(RSSchema schema, String fieldName) {
        RSField field = schema.getField(fieldName);
        return field != null;
    }

    protected abstract boolean checkValid(RSSchema schema);

    protected abstract String getErrorMsg(RSSchema schema);

    protected abstract String getLocalizedErrorMsg(RSSchema schema);

    public boolean isCascadedWith(Transformer followingTransformer) {
        return false;
    }
}
