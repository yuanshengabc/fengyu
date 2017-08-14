package cn.deepclue.datamaster.cleaner.domain.fusion;

/**
 * Created by magneto on 17-5-17.
 */
public enum FusionTaskStatus {
    UNCALCULATED(0), CALCULATING(1), CALCULATED(2), FINISHED(3), FAILED(4);

    private int status;

    FusionTaskStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static FusionTaskStatus getStatus(int status) {
        switch (status) {
            case 0:
                return UNCALCULATED;
            case 1:
                return CALCULATING;
            case 2:
                return CALCULATED;
            case 3:
                return FINISHED;
            case 4:
                return FAILED;
            default:
                throw new IllegalStateException("Unknown fusion task status: " + status);
        }
    }
}
