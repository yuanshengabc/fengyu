package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;

/**
 * Created by xuzb on 28/03/2017.
 */
public interface Writer {
    void writeSchema(RSSchema schema);
    void writeRecord(Record record);
    void open();
    void close();
}
