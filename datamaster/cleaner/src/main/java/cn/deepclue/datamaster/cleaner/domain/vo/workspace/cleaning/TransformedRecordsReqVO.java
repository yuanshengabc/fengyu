package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by xuzb on 19/04/2017.
 */
public class TransformedRecordsReqVO {
    @NotNull(message = "wsid can not be null.")
    private Integer wsid;

    @NotNull(message = "wsversion can not be null.")
    private Integer wsversion;

    @Min(0)
    @Max(value = 1000, message = "Page index must not be greater than 1000.")
    private int page = 0;

    @Min(1)
    @Max(value = 100, message = "Page size must not be greater than 100.")
    private int pageSize = 10;

    private String orderBy;

    private boolean asc = true;

    private List<FilterVO> filters;

    public Integer getWsid() {
        return wsid;
    }

    public Integer getWsversion() {
        return wsversion;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public Boolean getAsc() {
        return asc;
    }

    public List<FilterVO> getFilters() {
        return filters;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public void setWsversion(Integer wsversion) {
        this.wsversion = wsversion;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public void setFilters(List<FilterVO> filters) {
        this.filters = filters;
    }
}
