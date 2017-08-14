package cn.deepclue.datamaster.streamer.transform.filter;

/**
 * Created by xuzb on 08/05/2017.
 */
public abstract class RangeFilter<T extends Number> extends SingleFieldFilter {

    private T maxValue;
    private T minValue;

    public RangeFilter(String sourceFieldName) {
        super(sourceFieldName);
    }

    @Override
    protected boolean acceptValue(Object value) {
        if (!(value instanceof Number)) {
            return false;
        }

        Number number = (Number) value;

        if (maxValue != null && minValue != null) {
            return lte(minValue, number) && gte(maxValue, number);
        } else if (maxValue != null) {
            return gte(maxValue, number);
        } else if (minValue != null) {
            return lte(minValue, number);
        } else {
            throw new IllegalStateException("Max value or min value must be set in range filter.");
        }
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public T getMinValue() {
        return minValue;
    }

    protected abstract boolean gte(T lv, Number rv);
    protected abstract boolean lte(T lv, Number rv);
}
