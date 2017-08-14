package cn.deepclue.datamaster.cleaner.domain.vo.data;

import java.util.List;

/**
 * Created by xuzb on 08/06/2017.
 */
public class RecordListVO {
    private long total;
    private List<RecordVO> records;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<RecordVO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordVO> records) {
        this.records = records;
    }
}
