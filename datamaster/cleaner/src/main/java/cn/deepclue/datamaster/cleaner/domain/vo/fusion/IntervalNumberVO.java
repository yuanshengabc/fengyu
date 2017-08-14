package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.fusion.domain.IntervalNumber;

import java.math.BigDecimal;

public class IntervalNumberVO {
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

    public IntervalNumberVO from(IntervalNumber intervalNumber, BigDecimal total) {
        this.setStart(new BigDecimal(String.valueOf(intervalNumber.getStart()))
                .divide(total, 10, BigDecimal.ROUND_HALF_UP).doubleValue());
        this.setEnd(new BigDecimal(String.valueOf(intervalNumber.getEnd()))
                .divide(total, 10, BigDecimal.ROUND_HALF_UP).doubleValue());
        this.setNumber(intervalNumber.getNumber());
        return this;
    }
}
