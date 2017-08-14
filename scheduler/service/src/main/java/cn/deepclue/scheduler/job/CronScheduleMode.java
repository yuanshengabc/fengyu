package cn.deepclue.scheduler.job;

import cn.deepclue.scheduler.domain.ScheduleInfo;

public class CronScheduleMode extends ScheduleMode {
    private String cronExpression;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public ScheduleMode from(ScheduleInfo scheduleInfo) {
        super.from(scheduleInfo);
        this.setCronExpression(scheduleInfo.getCronScheduleInfo().getCronExpression());
        return this;
    }
}
