package cn.deepclue.datamaster.cleaner.domain.bo.task;

/**
 * Created by xuzb on 14/03/2017.
 * PENDING -- 等待调度
 * RUNNING -- 正在运行
 * FINISHED -- 运行成功
 * CANCLE -- 取消调度
 * FAILED -- 运行失败
 */
public enum TaskStatus {
    PENDING(0), RUNNING(1), FINISHED(2), CANCLE(3), FAILED(-1);

    private int value;

    TaskStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskStatus getTaskStatus(int status) {
        switch (status) {
            case 0:
                return PENDING;
            case 1:
                return RUNNING;
            case 2:
                return FINISHED;
            case 3:
                return CANCLE;
            case -1:
                return FAILED;

            default:
                throw new IllegalStateException("Unknown task status: " + status);
        }
    }
}
