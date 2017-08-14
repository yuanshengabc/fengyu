package cn.deepclue.datamaster.streamer.session;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.exception.MysqlException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by luoyong on 17-4-27.
 */
public class MysqlBulkInsert {
    private static Logger logger = LoggerFactory.getLogger(MysqlBulkInsert.class);
    private static final int BATCH_SIZE = 10000;
    private static final int PARALLEL_SIZE = 5;
    private final ExecutorService executor =
            new ThreadPoolExecutor(PARALLEL_SIZE, PARALLEL_SIZE, 10, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());

    private volatile DataSource dataSource;
    private volatile BaseType[] columnTypes;
    private volatile String insertSql;
    private List<Record> cacheRecords = Collections.synchronizedList(new ArrayList<>(BATCH_SIZE));
    private volatile boolean isEndBulk = false;
    private ThreadLocal<PreparedStatement> statementThreadLocal;

    public MysqlBulkInsert(MySQLTableConfig config, RSSchema schema) {
        initSchema(schema, config.getTableName());
        initDataSource(config.getMysqlConfig());

        statementThreadLocal = ThreadLocal.withInitial(() -> {
            try {
                return dataSource.getConnection().prepareStatement(insertSql);
            } catch (SQLException e) {
                logger.error("PreparedStatement insert sql error", e);
                throw new MysqlException("PreparedStatement insert sql error", "预处理插入语句异常。");
            }
        });
    }

    private void initDataSource(MySQLConfig dbConfig) {
        PoolProperties p = new PoolProperties();
        p.setUrl(dbConfig.getConnectionUrl());
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(dbConfig.getUsername());
        p.setPassword(dbConfig.getPassword());
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(PARALLEL_SIZE + 1);//+1 是由于CallerRunsPolicy 会利用住线程进行执行，所以线程数+1
        p.setInitialSize(PARALLEL_SIZE);
        p.setMinIdle(PARALLEL_SIZE);
        p.setMaxIdle(PARALLEL_SIZE + 1);
        p.setCommitOnReturn(true);
        p.setDefaultAutoCommit(false);
        p.setRemoveAbandonedTimeout(24 * 60 * 60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        this.dataSource = new DataSource(p);
    }

    private void initSchema(RSSchema schema, String tableName) {
        if (schema == null || schema.getFields() == null
                || schema.getFields().isEmpty()) {
            logger.error("BulkInsert 非法参数 schema");
            throw new IllegalArgumentException("BulkInsert 非法参数 schema");
        }
        int length = schema.getFields().size();
        String[] columnNames = new String[length];
        columnTypes = new BaseType[length];
        for (int i = 0; i < length; i++) {
            columnNames[i] = schema.getFields().get(i).getName();
            columnTypes[i] = schema.getFields().get(i).getBaseType();
        }

        insertSql = String.format("insert into `%s` (`%s`) values (%s)",
                tableName,
                String.join("`,`", columnNames),
                String.join(",", Collections.nCopies(schema.getFields().size(), "?")));
    }


    public void addInsert(Record record) {
        if (isEndBulk) {
            throw new MysqlException("MysqlBulkInsert can't call addInsert after endBulk", "MysqlBulkInsert不能在endBulk后调用addInsert.");
        }
        cacheRecords.add(record);
        if (cacheRecords.size() >= BATCH_SIZE) {
            List<Record> batchRecords = cacheRecords;
            cacheRecords = Collections.synchronizedList(new ArrayList<>(BATCH_SIZE));
            executor.execute(() -> {
                try {
                    batchInsert(batchRecords);
                } catch (SQLException e) {
                    logger.error("插入数据库异常", e);
                } catch (Exception e) {
                    logger.error("执行批量插入出现未知异常", e);
                }
            });
        }
    }


    private void batchInsert(List<Record> records) throws SQLException {
        PreparedStatement statement = statementThreadLocal.get();
        if (statement == null) {
            throw new MysqlException("Get mysql connection exception.", "获取mysql连接异常");
        }
        for (Record record : records) {
            List<Object> columnValues = record.getValues();
            for (int i = 0; i < columnValues.size(); i++) {
                int parameterIndex = i + 1;
                Object columnValue = columnValues.get(i);
                if (columnValue == null) {
                    statement.setObject(parameterIndex, null);
                    continue;
                }
                switch (columnTypes[i]) {
                    case DATE:
                        Date date = (Date) columnValues.get(i);
                        statement.setTimestamp(parameterIndex, date == null ? null : new Timestamp(date.getTime()));
                        break;
                    case DOUBLE:
                        statement.setDouble(parameterIndex, (Double) columnValues.get(i));
                        break;
                    case INT:
                        statement.setInt(parameterIndex, (Integer) columnValues.get(i));
                        break;
                    case LONG:
                        statement.setLong(parameterIndex, (Long) columnValues.get(i));
                        break;
                    case FLOAT:
                        statement.setFloat(parameterIndex, (Float) columnValues.get(i));
                        break;
                    case TEXT:
                        statement.setString(parameterIndex, (String) columnValues.get(i));
                        break;
                    default:
                        throw new IllegalStateException("不存在类型 BaseType" + columnTypes[i]);

                }
            }

            statement.addBatch();
        }

        statement.executeBatch();
        statement.getConnection().commit();
    }

    public void endBulk() {
        isEndBulk = true;
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("等待writer线程退出异常", e);
            Thread.currentThread().interrupt();
        }

        if (cacheRecords != null && !cacheRecords.isEmpty()) {
            try {
                batchInsert(cacheRecords);
            } catch (SQLException e) {
                logger.error("MysqlBulkInsert批量执行缓存待插入记录异常", e);
            }
        }

        dataSource.close();
    }

}
