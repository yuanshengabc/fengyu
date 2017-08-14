package cn.deepclue.datamaster.streamer.session;

import cn.deepclue.datamaster.streamer.session.esfilter.*;
import cn.deepclue.datamaster.streamer.transform.filter.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magneto on 17-4-14.
 */
public class ESFilterBuilder {
    private ESFilterBuilder() {

    }

    public static List<ESFilter> build(List<Filter> streamFilters) {
        List<ESFilter> esFilters = new ArrayList<>();
        if (streamFilters != null && ! streamFilters.isEmpty()) {
            // Construct es filter conditions
            for (Filter filter : streamFilters) {
                ESFilter esFilter;
                if (filter instanceof TextFilter) {
                    TextFilter textFilter = (TextFilter) filter;
                    if (textFilter.getMatchType() == TextFilter.MatchType.REGEX) {
                        esFilter = new ESRegexFilter(textFilter.getSourceFieldName(),
                                textFilter.getText(), false);
                    } else if (textFilter.getMatchType() == TextFilter.MatchType.REGEX_NEG) {
                        esFilter = new ESRegexFilter(textFilter.getSourceFieldName(),
                                textFilter.getText(), true);
                    } else {
                        esFilter = new ESTextFilter(textFilter.getSourceFieldName(),
                                textFilter.getText(),
                                convertToESMatchType(textFilter.getMatchType()));
                    }
                } else if (filter instanceof DateFilter) {
                    DateFilter dateFilter = (DateFilter) filter;
                    esFilter = new ESDateRangeFilter(dateFilter.getSourceFieldName(),
                                                     dateFilter.getStartDate()==null?null:dateFilter.getStartDate().getTime(),
                                                     dateFilter.getEndDate()==null?null:dateFilter.getEndDate().getTime());

                } else if (filter instanceof LongRangeFilter) {
                    LongRangeFilter longRangeFilter = (LongRangeFilter) filter;
                    esFilter = new ESRangeFilter<Long>( longRangeFilter.getSourceFieldName(),
                                                        longRangeFilter.getMinValue(),
                                                        longRangeFilter.getMaxValue());
                } else if (filter instanceof NotNullFilter) {
                    NotNullFilter notNullFilter = (NotNullFilter) filter;
                    esFilter = new ESNotNullFilter(notNullFilter.getSourceFieldName());
                } else if (filter instanceof DoubleRangeFilter) {
                    DoubleRangeFilter doubleRangeFilter = (DoubleRangeFilter) filter;
                    esFilter = new ESRangeFilter<Double>(doubleRangeFilter.getSourceFieldName(),
                            doubleRangeFilter.getMinValue(), doubleRangeFilter.getMaxValue());
                }
                else {
                    throw new IllegalArgumentException(String.format("unkonwn filter: %s!", filter.getClass()));
                }

                esFilters.add(esFilter);
            }
        }

        return esFilters;
    }

    public static ESTextFilter.ESMatchType convertToESMatchType(TextFilter.MatchType matchType) {
        switch (matchType) {
            case EQUALS:
                return ESTextFilter.ESMatchType.EXACT;
            case CONTAINS:
                return ESTextFilter.ESMatchType.CONTAINS;
            case STARTS:
                return ESTextFilter.ESMatchType.PREFIX;
            default:
                throw new IllegalArgumentException(String.format("unkonwn MatchType:%s", matchType));
        }
    }
}
