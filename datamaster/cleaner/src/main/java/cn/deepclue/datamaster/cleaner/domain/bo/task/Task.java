package cn.deepclue.datamaster.cleaner.domain.bo.task;

import java.util.Date;

/**
 * Created by xuzb on 14/03/2017.
 * Task for persistence
 */
public class Task {
    private Integer tid;
    private Integer status;
    private Integer sourceId;
    private String name;
    private Integer sinkId;
    private Integer type;
    private Date createdOn;
    private Date modifiedOn;
    private String extraData;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSinkId() {
        return sinkId;
    }

    public void setSinkId(Integer sinkId) {
        this.sinkId = sinkId;
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

    public TaskStatus getTaskStatus() {
        return TaskStatus.getTaskStatus(status);
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.status = taskStatus.getValue();
    }

    public TaskType getTaskType() {
        return TaskType.getTaskType(type);
    }

    public void setTaskType(TaskType taskType) {
        this.type = taskType.getType();
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
