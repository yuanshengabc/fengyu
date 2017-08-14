package cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning;


/**
 * UNCLEANED -- 未清洗
 * UNEXPORTED -- 未导出
 * EXPORTED -- 已导出
 * Created by magneto on 17-4-28.
 */
public enum WorkspaceEditionStatus {
    UNCLEANED(0), UNEXPORTED(1), EXPORTING(2), EXPORTED(3);

    private int value;
    WorkspaceEditionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static WorkspaceEditionStatus getWorkspaceEditionStatus(int value) {
        switch (value) {
            case 0:
                return UNCLEANED;
            case 1:
                return UNEXPORTED;
            case 2:
                return EXPORTING;
            case 3:
                return EXPORTED;
            default:
                throw new IllegalStateException("Unknown workspace status: " + value);
        }
    }
}
