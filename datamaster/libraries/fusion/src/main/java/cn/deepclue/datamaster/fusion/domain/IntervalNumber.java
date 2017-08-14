package cn.deepclue.datamaster.fusion.domain;

/**
 * Created by xuzb on 23/05/2017.
 * 左闭右开
 */
public class IntervalNumber {
    private double start;
    private double end;
    private long number;

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
