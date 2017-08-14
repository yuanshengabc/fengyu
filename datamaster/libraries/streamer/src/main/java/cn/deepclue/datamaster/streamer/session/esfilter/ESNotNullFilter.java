package cn.deepclue.datamaster.streamer.session.esfilter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * 非空域
 * Created by magneto on 17-4-13.
 */
public class ESNotNullFilter extends ESSingleFieldFilter{
    public ESNotNullFilter(String field) {
        super(field);
    }

    @Override
    public QueryBuilder filter() {
        return QueryBuilders.existsQuery(field);
    }
}
