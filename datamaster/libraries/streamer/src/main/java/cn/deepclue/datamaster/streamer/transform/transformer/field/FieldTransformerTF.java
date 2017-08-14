package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

/**
 * Created by xuzb on 14/04/2017.
 */
public abstract class FieldTransformerTF extends Transformer {

    private RSSchema targetSchema;

    @Override
    public RSSchema prepareValidSchema(RSSchema schema) {
        targetSchema = RSSchema.deepCopy(schema);
        prepareSchemaCopy(targetSchema);
        return targetSchema;
    }

    protected abstract void prepareSchemaCopy(RSSchema schemaCopy);
    protected abstract void transformRecordCopy(Record recordCopy);

    @Override
    public Record transform(Record record) {
        // We always make a record to preserve original records.
        Record recordCopy = Record.deepCopy(record);
        transformRecordCopy(recordCopy);

        recordCopy.setSchema(targetSchema);

        return recordCopy;
    }
}
