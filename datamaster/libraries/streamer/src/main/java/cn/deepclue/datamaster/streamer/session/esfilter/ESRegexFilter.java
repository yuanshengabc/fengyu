package cn.deepclue.datamaster.streamer.session.esfilter;

import cn.deepclue.datamaster.streamer.exception.RegexFilterException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by magneto on 17-5-27.
 */
public class ESRegexFilter extends ESSingleFieldFilter {
    private String regex;
    private boolean neg;

    public ESRegexFilter(String field, String regex, boolean neg) {
        super(field);
        this.regex = regex;
        this.neg = neg;
    }

    @Override
    public QueryBuilder filter() {
        String rawField = field + ".raw";

        QueryBuilder regexQuery;
        try {
            regexQuery = QueryBuilders.regexpQuery(rawField, regex);
        } catch (IllegalArgumentException e) {
            throw new RegexFilterException(e.getMessage(), "非法正则表达式！", e);
        }

        if (neg) {
            return QueryBuilders.boolQuery().mustNot(regexQuery);
        } else {
            return regexQuery;
        }
    }
}
