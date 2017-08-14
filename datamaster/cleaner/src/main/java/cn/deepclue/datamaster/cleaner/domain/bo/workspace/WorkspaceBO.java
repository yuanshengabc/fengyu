package cn.deepclue.datamaster.cleaner.domain.bo.workspace;

import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by magneto on 17-5-15.
 */
public abstract class WorkspaceBO {
    private Integer wsid;
    private Integer uid;
    private Integer wstype;
    private String name;
    private String description;
    private Date createdOn;
    private int finished;
    private Date modifiedOn;

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

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

    public WorkspaceType getWstype() {
        return WorkspaceType.getWorkspaceType(wstype);
    }

    public void setWstype(WorkspaceType wstype) {
        this.wstype = wstype.getType();
    }

    @JsonIgnore
    public FinishedStatus getFinished() {
        return FinishedStatus.getStatus(finished);
    }

    public void setStatus(FinishedStatus status) {
        this.finished = status.getValue();
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
