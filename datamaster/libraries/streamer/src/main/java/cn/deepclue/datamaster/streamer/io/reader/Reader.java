package cn.deepclue.datamaster.streamer.io.reader;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;

/**
 * Created by xuzb on 28/03/2017.
 */
public interface Reader {
    RSSchema readSchema();

    boolean hasNext();
    Record readRecord();

    void open();
    void close();
}
