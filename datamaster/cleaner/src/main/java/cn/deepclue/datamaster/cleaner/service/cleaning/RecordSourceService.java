package cn.deepclue.datamaster.cleaner.service.cleaning;

import cn.deepclue.datamaster.model.schema.RSSchema;

/**
 * Created by xuzb on 08/04/2017.
 */
public interface RecordSourceService {
    /**
     * Get schema of the data source.
     * @param rsid target record source id.
     * @return data source schema.
     */
    RSSchema getSchema(int rsid);
}
