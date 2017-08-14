package cn.deepclue.datamaster.cleaner.domain.po;

import java.util.Date;

/**
 * Created by xuzb on 14/03/2017.
 */
public class WorkspacePO {
    private Integer wsid;
    private Integer wstype;
    private int finished;
    private Integer uid;
    private String name;
    private String description;
    private Date createdOn;
    private Date modifiedOn;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getWstype() {
        return wstype;
    }

    public void setWstype(Integer wstype) {
        this.wstype = wstype;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
