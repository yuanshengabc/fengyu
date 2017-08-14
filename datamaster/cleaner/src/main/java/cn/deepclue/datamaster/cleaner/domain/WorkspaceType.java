package cn.deepclue.datamaster.cleaner.domain;

/**
 * Created by magneto on 17-5-15.
 */
public enum WorkspaceType {
    CLEANING(0), FUSION(1);

    private int type;

    WorkspaceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static WorkspaceType getWorkspaceType(int type) {
        switch (type) {
            case 0:
                return CLEANING;
            case 1:
                return FUSION;
            default:
                throw new IllegalStateException("Unknown workspace type: " + type);
        }
    }
}
