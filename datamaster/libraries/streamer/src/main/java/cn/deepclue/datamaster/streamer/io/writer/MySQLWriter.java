package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.TypeConverters;
import cn.deepclue.datamaster.streamer.io.consts.MysqlDataType;
import cn.deepclue.datamaster.streamer.session.MySQLSession;
import cn.deepclue.datamaster.streamer.session.MysqlBulkInsert;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import java.util.Collection;

/**
 * Created by luoyong on 17-4-6.
 */
public class MySQLWriter implements Writer {
    private MySQLSession session;
    private MySQLTableConfig config;
    private volatile MysqlBulkInsert mysqlBulkInsert;

    public MySQLWriter(MySQLTableConfig config) {
        this.config = config;
        session = new MySQLSession(config);
    }

    @Override
    public void writeSchema(RSSchema schema) {
        if (config.getTableName() == null) {
            throw new IllegalArgumentException("数据库表名不能为空");
        }
        session.execute(" drop table if exists `" + config.getTableName() + "`");
        StringBuilder ddlSql = new StringBuilder("create table ").append("`" + config.getTableName() + "`").append(" ( ");
        Collection<String> columns = Collections2.transform(schema.getFields(),
                field -> {
                    MysqlDataType dataType = TypeConverters.mysqlTypeConverter.convert(field.getBaseType());
                    return "`" + field.getName() + "`" + " " + dataType;
                });
        Joiner.on(",").appendTo(ddlSql, columns);
        ddlSql.append(") DEFAULT CHARSET=utf8mb4");
        session.execute(ddlSql.toString());
        this.mysqlBulkInsert = new MysqlBulkInsert(config, schema);
    }

    @Override
    public void writeRecord(Record record) {
        mysqlBulkInsert.addInsert(record);
    }

    @Override
    public void open() {
        if (session != null) {
            session.connect();
        }
    }

    @Override
    public void close() {
        if (session != null) {
            session.close();
        }
        if (mysqlBulkInsert != null) {
            mysqlBulkInsert.endBulk();
        }
    }


}
