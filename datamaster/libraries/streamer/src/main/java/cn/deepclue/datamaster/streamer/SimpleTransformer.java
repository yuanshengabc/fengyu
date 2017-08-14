package cn.deepclue.datamaster.streamer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.Transformation;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 11/04/2017.
 */
public class SimpleTransformer {

    private List<Transformer> transformers;

    public SimpleTransformer(List<Transformation> transformations) {
        transformers = TransformHelper.buildTransformers(transformations);
    }

    public RSSchema transformSchema(RSSchema schema) {
        return prepareSchema(schema);
    }

    private RSSchema prepareSchema(RSSchema schema) {
        RSSchema targetSchema = schema;
        for (Transformer transformer : transformers) {
            targetSchema = transformer.prepareSchema(targetSchema);
        }

        return targetSchema;
    }

    public List<Record> transformRecords(RSSchema schema, List<Record> records) {
        prepareSchema(schema);

        // Transform records
        List<Record> targetRecords = new ArrayList<>(records.size());
        for (Record record : records) {
            Record targetRecord = record;
            for (Transformer transformer : transformers) {
                targetRecord = transformer.transform(targetRecord);
            }

            targetRecords.add(targetRecord);
        }

        return targetRecords;
    }

}
