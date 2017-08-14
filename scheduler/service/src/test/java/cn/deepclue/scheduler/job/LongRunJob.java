package cn.deepclue.scheduler.job;


import cn.deepclue.scheduler.exception.QuartzException;

/**
 * Created by magneto on 17-3-24.
 */
public class LongRunJob extends Job {
    private long time;

    public LongRunJob(int jId, long time) {
        super(jId);
        this.time = time;
    }

    @Override
    public boolean run() {
        try {
            System.out.println(String.format("开始执行任务%d...", jId));
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            throw new QuartzException("LongRunJob run exception.", "LongRunJob运行失败。");
        }
        System.out.println(String.format("执行任务%d结束...", jId));
        return true;
    }

    @Override
    public String serialize() {
        return String.valueOf(time);
    }

    @Override
    public void deserialize(String jsonMap) {
        time = Long.parseLong(jsonMap);
    }

    public long getTime() {
        return time;
    }

    @Override
    public String getGroupName() {
        return getClass().getSimpleName();
    }
}
