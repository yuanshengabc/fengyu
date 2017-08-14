package cn.deepclue.datamaster.fusion.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 09/05/2017.
 */
public class PreviewStats {
    private List<IntervalNumber> intervalNumbers = new ArrayList<>();

    public List<IntervalNumber> getIntervalNumbers() {
        return intervalNumbers;
    }

    public void setIntervalNumbers(List<IntervalNumber> intervalNumbers) {
        this.intervalNumbers = intervalNumbers;
    }

    public void addIntervalNumber(IntervalNumber intervalNumber) {
        intervalNumbers.add(intervalNumber);
    }
}
