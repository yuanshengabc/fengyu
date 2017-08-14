package cn.deepclue.scheduler.job;

import cn.deepclue.scheduler.domain.ScheduleInfo;

public class SimpleScheduleMode extends ScheduleMode {
    private int repeatCount = 1;
    private long repeatInterval = 1000;

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    @Override
    public ScheduleMode from(ScheduleInfo scheduleInfo) {
        super.from(scheduleInfo);
        if (scheduleInfo.getSimpleScheduleInfo().getRepeatCount() != null) {
            this.setRepeatCount(scheduleInfo.getSimpleScheduleInfo().getRepeatCount());
        }
        if (scheduleInfo.getSimpleScheduleInfo().getRepeatInterval() != null) {
            this.setRepeatInterval(scheduleInfo.getSimpleScheduleInfo().getRepeatInterval());
        }

        return this;
    }
}
