package cn.deepclue.datamaster.streamer.impl;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

/**
 * Created by xuzb on 03/05/2017.
 */
public class HDFS2MySQLStreamer implements Streamer {
    @Override
    public boolean start() {
        // TODO:
        return false;
    }

    @Override
    public void addSource(Source source) {
        // TODO:
    }

    @Override
    public void addSink(Sink sink) {
        // TODO:
    }

    @Override
    public void registerTransform(Transformer transformer) {

    }
}
