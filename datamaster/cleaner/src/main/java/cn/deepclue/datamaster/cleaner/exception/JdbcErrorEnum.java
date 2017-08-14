package cn.deepclue.datamaster.cleaner.exception;

/**
 * 数据库异常枚举（错误码： 2XXX）
 * <p>
 * Created by luoyong on 17-4-21.
 */
public enum JdbcErrorEnum implements ErrorCode {
    INSERT(2001, "Failed to insert data.", "插入失败"),
    SELECT(2002, "Failed to select data.", "查询出错"),
    UPDATE(2003, "Failed to update data.", "更新出错"),
    DELETE(2004, "Failed to delete data.", "删除异常"),

    FETCH_DB_INFO(2011, "Failed to get databases.", "拉取数据库信息失败。"),
    FETCH_TABLE_INFO(2012, "Failed to get tables in database.", "获取数据库表信息失败。"),
    CREATE_DATABASE(2013, "Failed to create database.", "新建数据库失败。"),
    DATABASE_DELETE(2014, "Failed to delete database.", "删除数据库失败。"),
    CREATE_DATAHOUSE(2015, "Failed to create data house.", "创建数据仓库失败。"),
    INSERT_DATASOURCE(2016, "Failed to insert data source.", "添加数据源失败。"),
    CREATE_RECORD_SOURCE(2017, "Failed to create record source.", "创建记录源失败。"),
    SAVE_RSSCHEMA(2018, "Failed to save rsschema.", "保存数据源业务模型失败。"),
    SELECT_RSSCHEMA(2019, "Failed to get rsschema by rsid.", "根据数据源id查询数据源业务模型失败。"),

    CREATE_TASK(2021, "Failed to insert task.", "添加任务失败。"),
    CREATE_WORKSPACE(2031, "Failed to insert workspace.", "添加工作空间失败。"),
    CREATE_WORKSPACE_EDITION(2032, "Failed to insert workspace edition.", "添加工作空间版本失败。"),

    UPDATE_WORKSPACE_EDITION(2033, "Failed to upgrade workspace edition.", "升级工作空间版本失败。"),

    CREATE_TRANS(2041, "Failed to insert transformation.", "添加清洗失败。"),
    ADD_ONTOLOGY(2050, "Failed to add ontology mapping.", "添加实体映射失败。"),
    CREATE_OBJECT_TYPE(2051, "Failed to create object type.", "创建数据类型失败。"),
    SELECT_OBJECT_TYPES(2052, "Failed to get object type list.", "获取对象类型列表异常。"),
    CREATE_PROPERTY_TYPE(2053, "Failed to create property type.", "创建属性类型失败。"),
    CREATE_PROPERTY_GROUP(2054, "Failed to create property group.", "创建属性组失败。"),
    ID_NOT_FOUND(2055, "Id not found in database table.", "数据库表中未查询到id。");

    private Integer code;
    private String message;
    private String localizedMessage;

    JdbcErrorEnum(Integer code, String message, String localizedMessage) {
        this.code = code;
        this.message = message;
        this.localizedMessage = localizedMessage;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }


}
