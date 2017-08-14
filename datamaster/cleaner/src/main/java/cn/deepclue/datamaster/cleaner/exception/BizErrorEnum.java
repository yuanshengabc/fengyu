package cn.deepclue.datamaster.cleaner.exception;

/**
 * 业务异常枚举
 * <p>
 * Created by luoyong on 17-4-24.
 */
public enum BizErrorEnum implements ErrorCode {
    WORKSPACE_IMPORTING_DELETE(1001, "Workspace is importing data, can't delete.", "工作空间导入数据中，不能删除。"),
    WORKSPACE_TRANFORMING_DELETE(1002, "Workspace is tranforming data, can't delete.", "工作空间清洗数据中，不能删除。"),
    WORKSPACE_EDITION_INCONSIST(1003, "Inconsistent workspace edition.", "工作空间版本不一致。"),
    WS_VERSION_INCONSIST(1004, "Inconsistent wsversion.", "工作空间版本不一致。"),
    TASK_STATUS_INVALID_FINISH(1005, "Task is pending or running.", "任务挂起或执行中。"),
    SCHEMA_IS_EMPTY(1006, "Schema is empty in table.", "表中未设置模型。"),
    UNSUPPORTED_PREVIEW_FIELD_TOP_VALUES(1007, "TopValues in transformed field is not supported.", "清洗中的列不支持预览。"),
    UNKNOWN_FIELD_NAME_IN_TRANSFORMED_SCHEMA(1008, "Unknown field name in schema.", "列在模型中不识别。"),
    WORKSPACE_NO_RULERS(1009, "No rulers included in this workspace edition.", "此版本没有设置规则。"),
    WORKSPACE_INVALID_OPERATION_AFTER_FINISH(1010, "Invalid operation on finished workspace.", "在已完成工作空间中无效的操作。"),
    WORKSPACE_WORKING_DELETE(1011, "Workspace is working, can't delete.", "工作空间正在运行任务，不能删除。"),
    WORKSPACE_ES_DELETE(1012, "Elasticsearch data deleted exceptionally.", "工作空间elasticsearch数据删除异常。"),
    WORKSPACE_KAFKA_DELETE(1013, "Kafka data deleted exceptionally.", "工作空间kafka数据删除异常。"),
    WORKSPACE_MYSQL_DELETE(1014, "Mysql data deleted exceptionally.", "工作空间mysql数据删除异常。"),
    WORKSPACE_INFO_INCOMPLETE(1015, "Workspace info is not completed.", "工作空间信息不完整。"),
    FUSION_WORKSPACE_STEP_EXCEPTION(1016, "Fusion workspace invalid step.", "融合空间非法步骤异常。"),
    FUSION_WORKSPACE_SELECT_DATASOURCE_EXCEPTION(1017, "Empty datasource exception.", "未选择数据源异常。"),
    FUSIONRESULT_SAVED(1018, "Fusion result has been saved.", "融合结果已经被保存过。"),
    FUSIONTASK_FIELD(1019, "Fusion task failed.", "融合任务失败."),
    DATAMASTERSOURCE_LOST(1020, "Datamaster source lost.", "星河数据源丢失。"),
    INVALID_PROPERTYGROUP(1021, "invalid property group", "无效的属性组"),
    DATAHOUSE_DELETED(1022, "datahouse is deleted", "数据库连接已被删除。");

    private Integer code;
    private String message;
    private String localizedMessage;

    BizErrorEnum(Integer code, String message, String localizedMessage) {
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
