package cn.deepclue.datamaster.fusion.stdin;

import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 18/05/2017.
 */
public class DBConfig {
    private String type;
    private String username;
    private String password;
    private String dbName;
    private String tableName;
    private String ip;
    private int port;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static DBConfig from(MySQLTableConfig tableConfig) {
        DBConfig dbConfig = new DBConfig();
        dbConfig.type = "MySQL";
        dbConfig.tableName = tableConfig.getTableName();

        MySQLConfig mySQLConfig = tableConfig.getMysqlConfig();

        dbConfig.dbName = mySQLConfig.getDatabase();
        dbConfig.password = mySQLConfig.getPassword();
        dbConfig.username = mySQLConfig.getUsername();
        dbConfig.ip = mySQLConfig.getIp();
        dbConfig.port = mySQLConfig.getPort();

        return dbConfig;
    }

    public static List<DBConfig> from(List<MySQLTableConfig> configs) {
        List<DBConfig> dbConfigs = new ArrayList<>();
        for (MySQLTableConfig config : configs) {
            dbConfigs.add(from(config));
        }

        return dbConfigs;
    }
}
