package cn.deepclue.datamaster.cleaner.domain.po;

import java.util.Date;

/**
 * Created by magneto on 17-5-18.
 */
public class WorkspaceSourcePO {
    private Integer wsdsid;
    //工作空间id
    private Integer wsid;
    //数据源id
    private Integer sid;
    //数据源类型
    private Integer stype;
    //创建时间
    private Date createdOn;

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

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getStype() {
        return stype;
    }

    public void setStype(Integer stype) {
        this.stype = stype;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
