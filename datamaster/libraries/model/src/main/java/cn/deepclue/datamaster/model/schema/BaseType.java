package cn.deepclue.datamaster.model.schema;

/**
 * Created by xuzb on 15/03/2017.
 */
public enum BaseType {
    TEXT(0), INT(1), LONG(2), FLOAT(3), DOUBLE(4), DATE(5);

    private int value;
    BaseType(int value) {
        this.value = value;
    }

    public static BaseType getBaseType(int value) {
        switch (value) {
            case 0:
                return TEXT;
            case 1:
                return INT;
            case 2:
                return LONG;
            case 3:
                return FLOAT;
            case 4:
                return DOUBLE;
            case 5:
                return DATE;

            default:
                throw new IllegalStateException("Unknown base type value: " + value);
        }
    }

    public int getValue() {
        return value;
    }
}
