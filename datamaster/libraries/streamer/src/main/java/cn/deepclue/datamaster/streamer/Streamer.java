package cn.deepclue.datamaster.streamer;

import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

public interface Streamer {
    boolean start();

    void addSource(Source source);

    void addSink(Sink sink);

    void registerTransform(Transformer transformer);
}
