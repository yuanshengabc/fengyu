package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DatabaseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.DatabaseMapper;
import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by magneto on 17-3-28.
 */
@Repository("databaseDao")
public class DatabaseImpl implements DatabaseDao {
    @Autowired
    private DatabaseMapper databaseMapper;

    @Override
    public Database getDatabase(int dhid, String dbname) {
        return databaseMapper.getDatabase(dhid, dbname);
    }

    @Override
    public List<Database> getDatabases(int dhid, int page, int pageSize) {
        return databaseMapper.getDatabases(dhid, page * pageSize, pageSize);
    }

    @Override
    public Database createDatabase(Database database) {
        if (databaseMapper.insertDatabase(database)) {
            return database;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_DATABASE);
    }

    @Override
    public Database deleteDatabase(int dhid, String dbname) {
        Database database = getDatabase(dhid, dbname);
        if (database != null && databaseMapper.deleteDatabase(dhid, dbname)) {
            return database;
        }

        throw new JdbcException(String.format("Failed to delete database with dhid: %d, dbname: %s.", dhid, dbname),
                String.format("根据数据仓库ID：%d，数据库名称：%s，删除数据库失败。", dhid, dbname),
                JdbcErrorEnum.DATABASE_DELETE);
    }

    @Override
    public boolean truncate() {
        return databaseMapper.truncate();
    }

    @Override
    public List<Database> deleteDatabases(int dhid) {
        return null;
    }
}
