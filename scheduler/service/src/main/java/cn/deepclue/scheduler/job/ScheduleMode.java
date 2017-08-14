package cn.deepclue.scheduler.job;

import cn.deepclue.scheduler.domain.ScheduleInfo;
import org.quartz.CronScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;

import java.util.Date;

public abstract class ScheduleMode {
    protected Date startTime;
    protected Date endTime;
    protected String triggerDescription;
    protected int triggerPriority = 5;

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

    public ScheduleMode from(ScheduleInfo scheduleInfo) {
        this.setStartTime(scheduleInfo.getStartTime());
        this.setEndTime(scheduleInfo.getEndTime());
        this.setTriggerDescription(scheduleInfo.getTriggerDescription());
        this.setTriggerPriority(scheduleInfo.getTriggerPriority());
        return this;
    }
}
