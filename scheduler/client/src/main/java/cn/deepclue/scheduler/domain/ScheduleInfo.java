package cn.deepclue.scheduler.domain;

import java.util.Date;

public class ScheduleInfo {
    private Date startTime;
    private Date endTime;
    private String triggerDescription;
    private int triggerPriority = 5;
    private SimpleScheduleInfo simpleScheduleInfo;
    private CronScheduleInfo cronScheduleInfo;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTriggerDescription() {
        return triggerDescription;
    }

    public void setTriggerDescription(String triggerDescription) {
        this.triggerDescription = triggerDescription;
    }

    public int getTriggerPriority() {
        return triggerPriority;
    }

    public void setTriggerPriority(int triggerPriority) {
        this.triggerPriority = triggerPriority;
    }

    public SimpleScheduleInfo getSimpleScheduleInfo() {
        return simpleScheduleInfo;
    }

    public void setSimpleScheduleInfo(SimpleScheduleInfo simpleScheduleInfo) {
        this.simpleScheduleInfo = simpleScheduleInfo;
    }

    public CronScheduleInfo getCronScheduleInfo() {
        return cronScheduleInfo;
    }

    public void setCronScheduleInfo(CronScheduleInfo cronScheduleInfo) {
        this.cronScheduleInfo = cronScheduleInfo;
    }
}
