package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.RecordSourceMapper;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by magneto on 17-4-5.
 */
@Repository("recordSourceDao")
public class RecordSourceImpl implements RecordSourceDao {

    @Autowired
    private RecordSourceMapper recordSourceMapper;


    @Override
    public RecordSource createRecordSource(DataSource dataSource) {
        RecordSource recordSource = new RecordSource();
        recordSource.setDsid(dataSource.getDsid());
        recordSource.setName(dataSource.getDtname());
        recordSource.setRSType(RecordSource.RSType.MYSQL);
        recordSource.setCreatedOn(new Date());
        if (recordSourceMapper.insertRecordSource(recordSource) && recordSource.getRsid() != null) {
            return recordSource;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_RECORD_SOURCE);
    }

    @Override
    public RecordSource createRecordSource(String name, RecordSource.RSType type) {
        RecordSource recordSource = new RecordSource();
        recordSource.setName(name);
        recordSource.setCreatedOn(new Date());
        recordSource.setRSType(type);
        if (recordSourceMapper.insertRecordSource(recordSource) && recordSource.getRsid() != null) {
            return recordSource;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_RECORD_SOURCE);
    }

    @Override
    public RecordSource getRecordSource(int rsid) {
        return recordSourceMapper.getRecordSource(rsid);
    }

    public RSSchema getRSSchema(int rsid) {
        RecordSource recordSource = recordSourceMapper.getRecordSource(rsid);
        RSSchema rsSchema = new RSSchema();
        rsSchema.setName(recordSource.getName());
        rsSchema.setFields(recordSourceMapper.getRSFields(rsid));

        if (rsSchema.count() == 0) {
            throw new JdbcException("Failed to get rsschema with rsid: " + rsid,
                    "根据数据源id：" + rsid + "获取数据源业务模型失败。",
                    JdbcErrorEnum.SELECT_RSSCHEMA);
        }

        return rsSchema;
    }

    @Override
    public void saveRSSchema(int rsid, RSSchema schema) {
        recordSourceMapper.deleteRSFields(rsid);

        List<RSField> fields = schema.getFields();

        if (recordSourceMapper.insertRSFields(rsid, fields) != fields.size()) {
            throw new JdbcException(JdbcErrorEnum.SAVE_RSSCHEMA);
        }
    }

    @Override
    public void insertRecordSource(RecordSource sink) {
        if (!recordSourceMapper.insertRecordSource(sink)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_RECORD_SOURCE);
        }
    }

    @Override
    public boolean deleteRecordSource(int rsid) {
        if (!recordSourceMapper.deleteRecordSource(rsid)) {
            throw new JdbcException(JdbcErrorEnum.DELETE);
        }

        return true;
    }
}
