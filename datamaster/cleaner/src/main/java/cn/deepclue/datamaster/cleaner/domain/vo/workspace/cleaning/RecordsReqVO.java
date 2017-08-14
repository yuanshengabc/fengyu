package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by xuzb on 19/04/2017.
 */
public class RecordsReqVO {

    @NotNull(message = "rsid can not be null.")
    private Integer rsid;

    @Min(0)
    @Max(value = 1000, message = "Page index must not be greater than 1000.")
    private int page = 0;

    @Min(1)
    @Max(value = 100, message = "Page size must not be greater than 100.")
    private int pageSize = 10;

    private String orderBy;

    private boolean asc = true;

    private List<FilterVO> filters;

    public Integer getRsid() {
        return rsid;
    }

    public void setRsid(Integer rsid) {
        this.rsid = rsid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public List<FilterVO> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterVO> filters) {
        this.filters = filters;
    }
}
