package cn.deepclue.datamaster.streamer.transform.filter;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;

/**
 * Created by xuzb on 06/04/2017.
 */
public interface Filter {
    void prepareSchema(RSSchema schema);
    boolean accept(Record record);
}
