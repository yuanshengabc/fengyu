package cn.deepclue.datamaster.streamer.config;

/**
 * Created by luoyong on 17-4-7.
 */
public class MySQLTableConfig implements Config {

    private MySQLConfig mysqlConfig;
    private String tableName;

    public MySQLConfig getMysqlConfig() {
        return mysqlConfig;
    }

    public void setMysqlConfig(MySQLConfig mysqlConfig) {
        this.mysqlConfig = mysqlConfig;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
