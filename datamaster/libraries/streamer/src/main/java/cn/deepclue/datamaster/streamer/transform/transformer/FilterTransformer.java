package cn.deepclue.datamaster.streamer.transform.transformer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.filter.Filter;

import java.util.List;

/**
 * Created by xuzb on 07/04/2017.
 */
public abstract class FilterTransformer extends Transformer {
    private List<Filter> filters;

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public RSSchema prepareValidSchema(RSSchema schema) {
        if (filters != null) {
            for (Filter filter : filters) {
                filter.prepareSchema(schema);
            }
        }
        return prepareSchemaTF(schema);
    }

    protected abstract RSSchema prepareSchemaTF(RSSchema schema);

    public boolean accept(Record record) {
        if (filters != null) {
            for (Filter filter : filters) {
                if (!filter.accept(record)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Record transform(Record record) {
        if (accept(record)) {
            return transformAccepted(record);
        }

        return record;
    }

    protected abstract Record transformAccepted(Record record);
}
