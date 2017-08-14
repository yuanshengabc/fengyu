package cn.deepclue.datamaster.cleaner.domain.bo.data;

import cn.deepclue.datamaster.model.Record;

import java.util.List;

/**
 * Created by xuzb on 08/06/2017.
 */
public class RecordList {
    private long total;

    private List<Record> records;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
