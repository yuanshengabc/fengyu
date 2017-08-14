package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import cn.deepclue.datamaster.streamer.exception.UnknownFilterException;
import cn.deepclue.datamaster.streamer.transform.filter.*;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuzb on 18/04/2017.
 */
public class FilterVO {
    private String filterType;
    private String sourceFieldName;

    // Range filter fields
    private String minValue;
    private String maxValue;

    // Text filter fields
    private String text;
    private Integer type;

    // Date filter fields
    private Long startDate;
    private Long endDate;

    public String getFilterType() {
        return filterType;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public String getMinValue() {
        return minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public String getText() {
        return text;
    }

    public Integer getType() {
        return type;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    @Transient
    public TextFilter.MatchType getMatchType() {
        return TextFilter.MatchType.getMatchType(type);
    }

    public static Filter toFilter(FilterVO filterVO) {
        switch (filterVO.getFilterType()) {
            case "RangeFilter": // FIXME: Remove this case. We just want to be compatible with old version.
            case "LongRangeFilter":
                if (filterVO.getMaxValue() == null && filterVO.getMinValue() == null) {
                    return null;
                }

                LongRangeFilter longRangeFilter = new LongRangeFilter(filterVO.getSourceFieldName());

                if (filterVO.getMaxValue() != null) {
                    longRangeFilter.setMaxValue(Long.parseLong(filterVO.getMaxValue()));
                }

                if (filterVO.getMinValue() != null) {
                    longRangeFilter.setMinValue(Long.parseLong(filterVO.getMinValue()));
                }
                return longRangeFilter;
            case "DoubleRangeFilter":
                if (filterVO.getMaxValue() == null && filterVO.getMinValue() == null) {
                    return null;
                }

                DoubleRangeFilter doubleRangeFilter = new DoubleRangeFilter(filterVO.getSourceFieldName());

                if (filterVO.getMaxValue() != null) {
                    doubleRangeFilter.setMaxValue(Double.parseDouble(filterVO.getMaxValue()));
                }

                if (filterVO.getMinValue() != null) {
                    doubleRangeFilter.setMinValue(Double.parseDouble(filterVO.getMinValue()));
                }
                return doubleRangeFilter;
            case "TextFilter":
                if (filterVO.type == null || filterVO.getText() == null) {
                    return null;
                }
                return new TextFilter(filterVO.getSourceFieldName(), filterVO.getText(), filterVO.getMatchType());

            case "DateFilter":
                if (filterVO.getStartDate() == null && filterVO.getEndDate() == null) {
                    return null;
                }

                DateFilter dateFilter = new DateFilter(filterVO.getSourceFieldName());
                if (filterVO.getStartDate() != null) {
                    dateFilter.setStartDate(new Date(filterVO.getStartDate()));
                }

                if (filterVO.getEndDate() != null) {
                    dateFilter.setEndDate(new Date(filterVO.getEndDate()));
                }
                return dateFilter;

            case "NotNullFilter":
                return new NotNullFilter(filterVO.getSourceFieldName());

            default:
                throw new UnknownFilterException("Unknown filter type: " + filterVO.getFilterType(), "");
        }
    }

    public static List<Filter> toFilters(List<FilterVO> filterVOList) {
        if (filterVOList == null) {
            return null;
        }

        List<Filter> filters = new ArrayList<>();
        for (FilterVO filterVO : filterVOList) {
            Filter filter = toFilter(filterVO);
            if (filter != null) {
                filters.add(toFilter(filterVO));
            }
        }

        return filters;
    }
}
