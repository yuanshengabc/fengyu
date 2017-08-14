package cn.deepclue.datamaster.streamer.io.reader;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.exception.MysqlException;
import cn.deepclue.datamaster.streamer.session.MySQLSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by xuzb on 29/03/2017.
 */
public class MySQLReader implements Reader {
    private static Logger logger = LoggerFactory.getLogger(MySQLReader.class);

    private MySQLSession session;
    private ResultSet resultSet;
    private RSSchema schema;

    public MySQLReader(MySQLTableConfig source) {
        session = new MySQLSession(source);
    }

    @Override
    public RSSchema readSchema() {
        this.schema = session.readTableSchema();
        return schema;
    }

    @Override
    public boolean hasNext() {
        if (resultSet == null) {
            this.resultSet = session.select(schema.getFields());
        }
        if (resultSet == null) {
            return false;
        }
        try {
            boolean hasNext = resultSet.next();
            if (!hasNext) {
                resultSet.close();
                resultSet = null;
            }
            return hasNext;
        } catch (SQLException e) {
            handSqlException("has next exception.", "判断是否还有待遍历的记录异常。", e);
        }
        return false;
    }

    @Override
    public Record readRecord() {
        Record record = new Record(schema);
        try {
            int columnCount = schema.count();
            for (int i = 1; i <= columnCount; i++) {
                Object columnValue = resultSet.getObject(i);
                if (columnValue == null) {
                    record.addValue(null);
                    continue;
                }

                RSField rsField = schema.getField(i - 1);
                switch (rsField.getBaseType()) {
                    case FLOAT:
                        columnValue = resultSet.getFloat(i);
                        break;
                    case INT:
                        columnValue = resultSet.getInt(i);
                        break;
                    case LONG:
                        columnValue = resultSet.getLong(i);
                        break;
                    case DOUBLE:
                        columnValue = resultSet.getDouble(i);
                        break;
                    case DATE:
                        columnValue = resultSet.getTimestamp(i);
                        break;
                    case TEXT:
                        columnValue = resultSet.getString(i);
                        break;
                    default:
                        columnValue = resultSet.getString(i);
                        logger.error("baseType:{} is not handle", schema.getField(i - 1).getBaseType());

                }

                record.addValue(columnValue);
            }

        } catch (SQLException e) {
            handSqlException("get record exception.", "读取查询结果集合异常。", e);
        }

        // Generate unique id for each mysql record.
        record.setKey(UUID.randomUUID().toString());

        return record;
    }

    @Override
    public void open() {
        if (session != null) {
            session.connect();
        }
    }

    @Override
    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                handSqlException("Failed to close result set.", "关闭查询失败。", e);
            }
        }
        if (session != null) {
            session.close();
        }
    }

    private void handSqlException(String msg, String localMsg, SQLException e) {
        logger.error("SQLException: {}, SQLState: {}, VendorError: {}, msg: {}",
                e.getMessage(), e.getSQLState(), e.getErrorCode(), msg);
        throw new MysqlException(msg, localMsg, e);
    }

}
