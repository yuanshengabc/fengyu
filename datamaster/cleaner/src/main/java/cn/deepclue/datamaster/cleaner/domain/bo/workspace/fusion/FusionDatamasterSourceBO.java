package cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;

import java.util.Date;

/**
 * Created by sunxingwen on 17-5-20.
 */
public class FusionDatamasterSourceBO {
    //关联id
    private Integer wsdsid;
    //工作空间id
    private Integer wsid;
    //星河数据源
    private DatamasterSourcePO datamasterSourcePO;
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

    public DatamasterSourcePO getDatamasterSourcePO() {
        return datamasterSourcePO;
    }

    public void setDatamasterSourcePO(DatamasterSourcePO datamasterSourcePO) {
        this.datamasterSourcePO = datamasterSourcePO;
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

    public int getRsid() {
        return datamasterSourcePO.getRsid();
    }
}
