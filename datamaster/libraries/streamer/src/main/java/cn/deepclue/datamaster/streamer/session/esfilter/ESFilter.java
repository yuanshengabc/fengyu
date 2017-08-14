package cn.deepclue.datamaster.streamer.session.esfilter;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * Created by magneto on 17-4-13.
 */
public interface ESFilter {
    QueryBuilder filter();
}
