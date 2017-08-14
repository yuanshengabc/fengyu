package cn.deepclue.datamaster.cleaner.domain.vo.data;

/**
 * Created by magneto on 17-4-7.
 */
public class RecordStatsRespVO {
    private int empty;
    private int nonempty;
    private double integrity;
    private int distinct;
    private int total;
    private double richness;
    private int repeat;

    public int getEmpty() {
        return empty;
    }

    public void setEmpty(int empty) {
        this.empty = empty;
    }

    public int getNonempty() {
        return nonempty;
    }

    public void setNonempty(int nonempty) {
        this.nonempty = nonempty;
    }

    public double getIntegrity() {
        return integrity;
    }

    public void setIntegrity(double integrity) {
        this.integrity = integrity;
    }

    public int getDistinct() {
        return distinct;
    }

    public void setDistinct(int distinct) {
        this.distinct = distinct;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getRichness() {
        return richness;
    }

    public void setRichness(double richness) {
        this.richness = richness;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
