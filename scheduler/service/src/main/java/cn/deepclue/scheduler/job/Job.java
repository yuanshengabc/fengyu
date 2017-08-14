package cn.deepclue.scheduler.job;

/**
 * Created by zhangren on 17/03/2017.
 */
public abstract class Job {
    protected static final String JID_KEY = "JID";
    protected static final String APPID_KEY = "APPID";
    protected static final String SCHEDULE_MODE = "SCHEDULEMODE";
    protected static final String SCHEDULE_MODE_TYPE = "SCHEDULEMODETYPE";

    protected int jId;
    protected int appId;
    protected ScheduleMode scheduleMode;

    public Job() {
    }

    public Job(int jId) {
        this.jId = jId;
    }

    public Job(int jId, int appId, ScheduleMode scheduleMode) {
        this.jId = jId;
        this.appId = appId;
        this.scheduleMode = scheduleMode;
    }

    public int getjId() {
        return jId;
    }

    public void setjId(int jId) {
        this.jId = jId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getGroupName() {
        return String.valueOf(appId);
    }

    public ScheduleMode getScheduleMode() {
        return scheduleMode;
    }

    public void setScheduleMode(ScheduleMode scheduleMode) {
        this.scheduleMode = scheduleMode;
    }

    public abstract boolean run();

    public abstract String serialize();

    public abstract void deserialize(String jsonMap);
}
