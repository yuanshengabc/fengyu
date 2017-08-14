package cn.deepclue.datamaster.streamer.session.esfilter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by magneto on 17-4-13.
 */
public class ESTextFilter extends ESSingleFieldFilter {
    private String text;
    private int type;

    public ESTextFilter(String field, String text, ESMatchType matchType) {
        super(field);
        this.text = text;
        setMatchType(matchType);
    }


    @Override
    public QueryBuilder filter() {
        if (text == null) {
            return QueryBuilders.matchAllQuery();
        }

        switch (getMatchType()) {
            case EXACT:
                String rawField = field + ".raw";
                return QueryBuilders.termQuery(rawField, text);
            case CONTAINS:
                String value = String.format("\"%s\"", text);
                return QueryBuilders.queryStringQuery(value).field(field);
            case PREFIX:
                return QueryBuilders.prefixQuery(field, text);

            default:
                throw new IllegalStateException("Unknown match type");
        }
    }

    public ESMatchType getMatchType() {
        return ESMatchType.getMatchType(type);
    }

    public void setMatchType(ESMatchType matchType) {
        this.type = matchType.getValue();
    }

    public enum ESMatchType{
        EXACT(0), CONTAINS(1), PREFIX(2);

        private int value;

        ESMatchType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ESMatchType getMatchType(int value) {
            switch (value) {
                case 0:
                    return EXACT;
                case 1:
                    return CONTAINS;
                case 2:
                    return PREFIX;

                default:
                    throw new IllegalStateException("Unknown match type: " + value);
            }
        }
    }
}
