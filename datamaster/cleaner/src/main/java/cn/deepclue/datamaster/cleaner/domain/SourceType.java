package cn.deepclue.datamaster.cleaner.domain;

/**
 * Created by magneto on 17-5-18.
 */
public enum SourceType {
    DATASOURCE(0), DATAMASTER_SOURCE(1);

    private int type;

    SourceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static SourceType getSourceType(int type) {
        switch (type) {
            case 0:
                return DATASOURCE;
            case 1:
                return DATAMASTER_SOURCE;
            default:
                throw new IllegalStateException("Unknown source type: " + type);
        }
    }
}
