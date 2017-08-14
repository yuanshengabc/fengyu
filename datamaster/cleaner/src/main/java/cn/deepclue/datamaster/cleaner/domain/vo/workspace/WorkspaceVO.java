package cn.deepclue.datamaster.cleaner.domain.vo.workspace;

import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;

import java.util.Date;

/**
 * Created by magneto on 17-5-15.
 */
public class WorkspaceVO {
    private int wsid;
    private int wstype;
    private String name;
    private String description;
    private Date createdOn;
    private Date modifiedOn;

    public int getWsid() {
        return wsid;
    }

    public void setWsid(int wsid) {
        this.wsid = wsid;
    }

    public WorkspaceType getWstype() {
        return WorkspaceType.getWorkspaceType(wstype);
    }

    public void setWstype(WorkspaceType wstype) {
        this.wstype = wstype.getType();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
