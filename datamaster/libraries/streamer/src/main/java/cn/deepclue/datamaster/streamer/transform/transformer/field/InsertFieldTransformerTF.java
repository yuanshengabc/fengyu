package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

/**
 * Created by xuzb on 24/04/2017.
 */
public abstract class InsertFieldTransformerTF extends Transformer {

    private int insertPosition;
    private RSSchema newSchema;

    @Override
    public RSSchema prepareValidSchema(RSSchema schema) {
        insertPosition = computeInsertPosition(schema);
        newSchema = new RSSchema();
        newSchema.setName(schema.getName());

        int i = 0;
        for (; i <= insertPosition; ++i) {
            newSchema.addField(RSField.deepCopy(schema.getField(i)));
        }

        newSchema.addField(createField(schema));

        for (; i < schema.count(); ++i) {
            newSchema.addField(RSField.deepCopy(schema.getField(i)));
        }

        return newSchema;
    }

    @Override
    public Record transform(Record record) {
        Record newRecord = new Record(newSchema);
        newRecord.setKey(record.getKey());

        int i = 0;
        for (; i <= insertPosition; ++i) {
            newRecord.addValue(record.getValue(i));
        }

        newRecord.addValue(createValue(record));

        for (; i < record.size(); ++i) {
            newRecord.addValue(record.getValue(i));
        }

        return newRecord;
    }

    protected abstract int computeInsertPosition(RSSchema schema);
    protected abstract RSField createField(RSSchema schema);
    protected abstract Object createValue(Record record);

    @Override
    public boolean isCascadedWith(Transformer followingTransformer) {
        return super.isCascadedWith(followingTransformer);
    }
}
