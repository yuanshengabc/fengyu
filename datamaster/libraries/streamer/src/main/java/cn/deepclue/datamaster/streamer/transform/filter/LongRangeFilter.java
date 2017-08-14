package cn.deepclue.datamaster.streamer.transform.filter;

/**
 * Created by xuzb on 06/04/2017.
 */
public class LongRangeFilter extends RangeFilter<Long> {

    public LongRangeFilter(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected boolean gte(Long lv, Number rv) {
        return lv >= rv.longValue();
    }

    @Override
    protected boolean lte(Long lv, Number rv) {
        return lv <= rv.longValue();
    }
}
