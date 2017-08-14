package cn.deepclue.datamaster.streamer.session.esfilter;

/**
 * 日期类型范围搜索
 * 日期类型，参数以long类型获取
 * Created by magneto on 17-4-13.
 */
public class ESDateRangeFilter extends ESRangeFilter<Long>{

    public ESDateRangeFilter(String field, Long lMin, Long lMax) {
        super(field, lMin, lMax);
    }

    @Override
    public void leftBound() {
        builder.gte(lMin);
    }

    @Override
    public void rightBound() {
        builder.lte(lMax);
    }
}
