package cn.deepclue.datamaster.cleaner.domain.vo.data;

/**
 * Created by xuzb on 14/03/2017.
 */
public class TopValueVO {
    private String value;
    private int count;

    public TopValueVO(String value, int count) {
        this.value = value;
        this.count = count;
    }

    public TopValueVO() {
        // Empty constructor
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
