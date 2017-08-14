package cn.deepclue.datamaster.streamer.session;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.exception.MysqlException;
import cn.deepclue.datamaster.streamer.io.TypeConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 29/03/2017.
 */
public class MySQLSession implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(MySQLSession.class);
    private MySQLTableConfig config;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private static String NOT_CONNECTED_MSG = "未建立数据库连接";

    public MySQLSession(MySQLTableConfig config) {
        this.config = config;
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            String errorMsg = "load com.mysql.jdbc.Driver error!";
            logger.error(errorMsg, ex);
            throw new MysqlException(errorMsg, "加载mysql驱动异常。", ex);
        }

        try {
            this.connection = DriverManager.getConnection(config.getMysqlConfig().getConnectionUrl());
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            handSqlException("dabase can not connect,url:" + config.getMysqlConfig().getConnectionUrl(),
                    "数据库连接不上,url:" + config.getMysqlConfig().getConnectionUrl(), e);
        }
        return true;
    }

    public boolean canConnect() {
        try {
            return connect();
        } catch (Exception e) {
            logger.info("can't connect db", e);
            return false;
        } finally {
            close();
        }
    }

    public ResultSet select(List<RSField> fields) {
        if (connection == null) {
            throw new MysqlException("database not connected.", NOT_CONNECTED_MSG);
        }
        StringBuilder sqlBuilder = new StringBuilder("select ");
        if (fields == null || fields.isEmpty()) {
            sqlBuilder.append(" * ");
        } else {
            for (RSField field : fields) {
                sqlBuilder.append("`").append(field.getName()).append("`");
                sqlBuilder.append(",");
            }
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(" from `").append(config.getTableName()).append("`");
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(sqlBuilder.toString(), ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            rs = preparedStatement.executeQuery(sqlBuilder.toString());
        } catch (SQLException e) {
            handSqlException("database select exception.", "数据库查询异常。", e);
        }
        return rs;
    }

    public List<String> getDatabases() {
        if (connection == null) {
            throw new MysqlException("database not connected.", NOT_CONNECTED_MSG);
        }
        List<String> databaseStrs = new ArrayList<>();
        String sql = "SHOW DATABASES";
        ResultSet rs = null;
        try {
            rs = this.statement.executeQuery(sql);
            while (rs.next()) {
                String databaseStr = rs.getString("Database");
                databaseStrs.add(databaseStr);
            }
        } catch (SQLException e) {
            String errorMsg = "获取数据库列表出现异常。";
            logger.warn(errorMsg, e);
            throw new MysqlException("get database list exception.", errorMsg, e);
        } finally {
            close(rs);
        }

        return databaseStrs;
    }

    public Integer getTableRows() {
        if (connection == null) {
            throw new MysqlException("database not connected.", NOT_CONNECTED_MSG);
        }
        String sql = "select table_rows from information_schema.tables where TABLE_SCHEMA = '"
                + config.getMysqlConfig().getDatabase() + "' AND table_name = '" + config.getTableName() + "'";
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt("table_rows");
            }
        } catch (SQLException e) {
            handSqlException(String.format("get database：%s, table%s row count exception.", config.getMysqlConfig().getDatabase(), config.getTableName()),
                    String.format("获取数据库：%s, 表%s 行数出错", config.getMysqlConfig().getDatabase(), config.getTableName()), e);
        }
        return null;
    }


    public List<TableInfo> getDataTables(String database) {
        return this.getDataTables(database, null, null);
    }

    public List<TableInfo> getDataTables(String database, Integer offset, Integer limit) {
        if (connection == null) {
            throw new MysqlException("database not connected", NOT_CONNECTED_MSG);
        }
        StringBuilder sqlBuilder = new StringBuilder("SELECT table_name, table_rows FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = \"")
                .append(database).append("\"");
        if (limit != null) {
            sqlBuilder.append("limit ").append(offset == null ? 0 : offset).append(", ").append(limit);
        }
        List<TableInfo> tableInfos = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = this.statement.executeQuery(sqlBuilder.toString());
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getString("table_name"));
                tableInfo.setTableRows(rs.getInt("table_rows"));
                tableInfos.add(tableInfo);
            }
        } catch (SQLException e) {
            String errorMsg = "获取数据库列表出现异常！";
            logger.warn(errorMsg, e);
            throw new MysqlException("get database list exception.", errorMsg, e);
        } finally {
            close(rs);
        }

        return tableInfos;
    }

    public Integer getDataTableCount(String database) {
        String sql = String.format("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = \"%s\"", database);

        ResultSet rs = null;
        try {
            rs = this.statement.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            String errorMsg = "获取数据库表数量出现异常！";
            logger.warn(errorMsg, e);
            throw new MysqlException("get database table count exception.", errorMsg, e);
        } finally {
            close(rs);
        }
    }

    public void execute(String sql) {
        if (connection == null) {
            throw new MysqlException("database not connected", NOT_CONNECTED_MSG);
        }
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            logger.error("sql:" + sql);
            handSqlException("sql execute exception.", "执行sql语句异常。", e);
        }

    }

    public void close() {

        try {
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            handSqlException("close database connection exception.", "关闭数据库连接异常", e);
        }
    }


    private void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                handSqlException("close mysql result set exception.", "关闭mysql结果集异常", e);
            }
        }
    }


    public static void handSqlException(String msg, String localMsg, SQLException e) {
        logger.error("SQLException: {}, SQLState: {}, VendorError: {}, msg: {}",
                e.getMessage(), e.getSQLState(), e.getErrorCode(), msg);
        throw new MysqlException(msg, localMsg, e);
    }

    public RSSchema readTableSchema() {
        RSSchema rsSchema = new RSSchema();
        rsSchema.setName(config.getTableName());

        try {
            DatabaseMetaData md = connection.getMetaData();
            try (ResultSet rs = md.getColumns(null,
                    "`" + config.getMysqlConfig().getDatabase() + "`",
                    "`" + config.getTableName() + "`",
                    null)) {
                while (rs.next()) {
                    int dataType = rs.getInt("DATA_TYPE");
                    BaseType baseType = TypeConverters.jsbcTypeConverter.convert(dataType);
                    String columnName = rs.getString("COLUMN_NAME");
                    if (baseType == null) {
                        logger.warn("表{}列{}的数据类型对应的JDBC types{}不在考虑范围，被忽略", config.getTableName(), columnName, dataType);
                        continue;
                    }
                    RSField field = new RSField();
                    field.setBaseType(baseType);
                    field.setName(columnName);
                    rsSchema.addField(field);
                }
                if (rsSchema.getFields() == null) {
                    throw new MysqlException("table" + config.getTableName() + " is not exists.", "表" + config.getTableName() + "不存在。");
                }
            }
        } catch (SQLException e) {
            handSqlException("get table info exception.", "获取table信息出错", e);
        }

        return rsSchema;
    }

}
