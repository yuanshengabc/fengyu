package cn.deepclue.datamaster.streamer.transform.filter;

/**
 * Created by xuzb on 08/05/2017.
 */
public class DoubleRangeFilter extends RangeFilter<Double> {
    public DoubleRangeFilter(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected boolean gte(Double lv, Number rv) {
        return lv >= rv.doubleValue();
    }

    @Override
    protected boolean lte(Double lv, Number rv) {
        return lv <= rv.doubleValue();
    }
}
