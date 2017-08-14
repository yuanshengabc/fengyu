package cn.deepclue.datamaster.cleaning.job;

import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.job.StreamerJob;
import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.io.rwstreamer.KafkaStreamsRWStreamer;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.Transformation;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

import java.util.List;

/**
 * Created by xuzb on 19/03/2017.
 */
public class TransformJob extends StreamerJob {
    public TransformJob() {}

    public TransformJob(int jId, KTopicConfig source, KTopicConfig sink, List<Transformation> transformations) {
        super(jId, source, sink);
        setTransformations(transformations);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return KTopicConfig.class;
    }

    @Override
    public Class<? extends Sink> getSinkClass() {
        return KTopicConfig.class;
    }

    @Override
    public Streamer newStreamer() {
        Streamer streamer = new KafkaStreamsRWStreamer();

        List<Transformer> transformers = TransformHelper.buildTransformers(getTransformations());

        for (Transformer transformer : transformers) {
            streamer.registerTransform(transformer);
        }

        return streamer;
    }

    @Override
    public String getGroupName() {
        return "TRANSFORM";
    }
}
