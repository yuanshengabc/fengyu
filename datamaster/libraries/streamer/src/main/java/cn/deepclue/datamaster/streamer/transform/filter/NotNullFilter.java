package cn.deepclue.datamaster.streamer.transform.filter;

/**
 * Created by xuzb on 14/04/2017.
 */
public class NotNullFilter extends SingleFieldFilter {
    public NotNullFilter(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected boolean acceptValue(Object value) {
        return value != null && !(value instanceof String && "".equals(value));
    }
}
