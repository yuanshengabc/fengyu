package cn.deepclue.datamaster.cleaner.domain.vo.data;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataTable;

import java.beans.Transient;
import java.util.List;

/**
 * Created by magneto on 17-4-12.
 */
public class DataTableListVO {
    private List<DataTable> dataTables;
    private Integer dsCount;

    public List<DataTable> getDataSources() {
        return dataTables;
    }

    @Transient
    public List<DataTable> getDataTables() {
        return dataTables;
    }

    public void setDataTables(List<DataTable> dataTables) {
        this.dataTables = dataTables;
    }

    public Integer getDsCount() {
        return dsCount;
    }

    public void setDsCount(Integer dsCount) {
        this.dsCount = dsCount;
    }
}
