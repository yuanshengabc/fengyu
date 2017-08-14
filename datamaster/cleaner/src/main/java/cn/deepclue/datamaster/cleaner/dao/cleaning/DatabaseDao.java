package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;

import java.util.List;

/**
 * Created by magneto on 17-3-28.
 */
public interface DatabaseDao {
    Database getDatabase(int dhid, String dbname);
    List<Database> getDatabases(int dhid, int page, int pageSize);

    Database createDatabase(Database database);

    Database deleteDatabase(int dhid, String dbname);
    List<Database> deleteDatabases(int dhid);

    boolean truncate();
}
