package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.model.schema.RSSchema;

/**
 * Created by xuzb on 17/03/2017.
 */
public interface RecordSourceDao {
    RecordSource createRecordSource(DataSource dataSource);
    RecordSource createRecordSource(String name, RecordSource.RSType type);
    RecordSource getRecordSource(int rsid);
    RSSchema getRSSchema(int rsid);
    void saveRSSchema(int rsid, RSSchema schema);

    void insertRecordSource(RecordSource sink);

    boolean deleteRecordSource(int rsid);
}
