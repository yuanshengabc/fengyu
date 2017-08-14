package cn.deepclue.datamaster.streamer.session.esfilter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

/**
 * 采用[]，左闭右闭语义
 * T 包括int,long,float,double
 * Created by magneto on 17-4-13.
 */
public class ESRangeFilter<T> extends ESSingleFieldFilter {
    protected RangeQueryBuilder builder;

    protected T lMin;
    protected T lMax;

    public ESRangeFilter(String field, T lMin, T lMax) {
        super(field);
        this.lMin = lMin;
        this.lMax = lMax;

        builder = QueryBuilders.rangeQuery(this.field);
    }

    @Override
    public final QueryBuilder filter() {
        if (lMin != null) {
            leftBound();
        }

        if (lMax != null) {
            rightBound();
        }

        return builder;
    }

    public void leftBound() {
        builder.gte(lMin);
    }

    public void rightBound() {
        builder.lte(lMax);
    }
}
