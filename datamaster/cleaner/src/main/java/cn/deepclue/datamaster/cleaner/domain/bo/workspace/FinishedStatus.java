package cn.deepclue.datamaster.cleaner.domain.bo.workspace;

/**
 * Created by magneto on 17-5-23.
 */
public enum FinishedStatus {
    FINISHED(1), UNFINISHED(0);

    private int value;
    FinishedStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static  FinishedStatus getStatus(int value) {
        switch (value) {
            case 0:
                return UNFINISHED; //
            case 1:
                return FINISHED;

            default:
                throw new IllegalStateException("Unknown workspace status: " + value);
        }
    }
}
