package cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;

import java.util.Date;

/**
 * 融合外部数据源BO
 * Created by sunxingwen on 17-5-20.
 */
public class FusionDataSourceBO {
    //关联id
    private Integer wsdsid;
    //工作空间id
    private Integer wsid;
    //数据源
    private DataSourceBO dataSourceBO;
    //创建时间
    private Date createdOn;
    //是否匹配
    private Boolean match;

    public Integer getWsdsid() {
        return wsdsid;
    }

    public void setWsdsid(Integer wsdsid) {
        this.wsdsid = wsdsid;
    }

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public DataSourceBO getDataSourceBO() {
        return dataSourceBO;
    }

    public void setDataSourceBO(DataSourceBO dataSourceBO) {
        this.dataSourceBO = dataSourceBO;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getMatch() {
        return match;
    }

    public void setMatch(Boolean match) {
        this.match = match;
    }
}
