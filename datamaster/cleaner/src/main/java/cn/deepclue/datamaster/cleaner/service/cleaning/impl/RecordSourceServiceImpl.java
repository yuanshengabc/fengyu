package cn.deepclue.datamaster.cleaner.service.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.service.cleaning.RecordSourceService;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuzb on 08/04/2017.
 */
@Service("recordSourceService")
public class RecordSourceServiceImpl implements RecordSourceService {

    @Autowired
    private RecordSourceDao recordSourceDao;

    @Override
    public RSSchema getSchema(int rsid) {
        return recordSourceDao.getRSSchema(rsid);
    }
}
