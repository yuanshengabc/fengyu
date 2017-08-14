package cn.deepclue.datamaster.cleaning.job;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.rwstreamer.Kafka2HDFSRWStreamer;
import cn.deepclue.datamaster.streamer.job.StreamerJob;

public class Persistence2HDFSJob extends StreamerJob {
    public Persistence2HDFSJob() {
        //for QScheduleJob.execute
    }

    public Persistence2HDFSJob(int tid, KTopicConfig source, HDFSFileConfig sink) {
        super(tid, source, sink);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return KTopicConfig.class;
    }

    @Override
    public Class<? extends Sink> getSinkClass() {
        return HDFSFileConfig.class;
    }

    @Override
    public Streamer newStreamer() {
        return new Kafka2HDFSRWStreamer();
    }

    @Override
    public String toString() {
        return "Persistence2HDFSJob{}";
    }

    @Override
    public String getGroupName() {
        return "PERSISTENCE";
    }
}
