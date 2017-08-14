package cn.deepclue.datamaster.cleaner.domain.bo.task;

/**
 * Created by xuzb on 17/03/2017.
 */
public enum TaskType {
    IMPORT(0), ANALYSIS(1), TRANSFORM(2), EXPORT(3),
    ENTROPY_COMPUTE(4), SIMILARITY_COMPUTE(5), PERSISTENCE(6);

    private int type;

    TaskType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static TaskType getTaskType(int type) {
        switch (type) {
            case 0:
                return IMPORT;
            case 1:
                return ANALYSIS;
            case 2:
                return TRANSFORM;
            case 3:
                return EXPORT;
            case 4:
                return ENTROPY_COMPUTE;
            case 5:
                return SIMILARITY_COMPUTE;
            case 6:
                return PERSISTENCE;

            default:
                throw new IllegalStateException("Unknown task type");
        }
    }
}
