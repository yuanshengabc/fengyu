package cn.deepclue.datamaster.streamer.transform.filter;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;

/**
 * Created by xuzb on 06/04/2017.
 */
public abstract class SingleFieldFilter implements Filter {
    private String sourceFieldName;

    private int fieldIndex = -1;

    public SingleFieldFilter(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    @Override
    public void prepareSchema(RSSchema schema) {
        fieldIndex = schema.getFieldIndex(sourceFieldName);

        if (fieldIndex == -1) {
            throw new IllegalStateException("Invalid source field name: " + sourceFieldName);
        }
    }

    @Override
    public boolean accept(Record record) {
        Object value = record.getValue(fieldIndex);
        return value != null && acceptValue(value);
    }

    protected abstract boolean acceptValue(Object value);

    public String getSourceFieldName() {
        return sourceFieldName;
    }
}
