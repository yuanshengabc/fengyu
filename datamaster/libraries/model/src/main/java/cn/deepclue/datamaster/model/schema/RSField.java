package cn.deepclue.datamaster.model.schema;

/**
 * Created by xuzb on 15/03/2017.
 */
public class RSField {
    private String name;
    private int type;

    public RSField() {
        // Empty constructor
    }

    public RSField(String name, BaseType baseType) {
        this.name = name;
        setBaseType(baseType);
    }

    public RSField(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseType getBaseType() {
        return BaseType.getBaseType(type);
    }

    public void setBaseType(BaseType baseType) {
        this.type = baseType.getValue();
    }

    public int getBaseTypeValue() {
        return type;
    }

    public static RSField deepCopy(RSField field) {
        return new RSField(field.name, field.type);
    }
}
