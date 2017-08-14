package cn.deepclue.datamaster.streamer.job;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.transform.Rewriter;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.Transformation;
import cn.deepclue.datamaster.streamer.util.JsonUtils;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuzb on 17/03/2017.
 */
public abstract class StreamerJob extends Job {

    public static final String JID_KEY = "JID";
    public static final String SOURCE_KEY = "SOURCE";
    public static final String SINK_KEY = "SINK";
    public static final String TRANSFORMATION_KEY = "TRANSFORMATION";
    public static final String REWRITER_KEY = "REWRITER";

    private Source source;
    private Sink sink;
    private List<Transformation> transformations = new ArrayList<>();
    private Rewriter rewriter = new Rewriter();

    public StreamerJob() {}

    public StreamerJob(int jId, Source source, Sink sink) {
        super(jId);
        this.source = source;
        this.sink = sink;
    }

    @Override
    public boolean run() {
        Streamer streamer = newStreamer();
        streamer.addSource(source);
        streamer.addSink(sink);

        streamer.start();

        return true;
    }

    @Override
    public String serialize() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put(JID_KEY, String.valueOf(jId));
        dataMap.put(SOURCE_KEY, JsonUtils.toJson(getSource()));
        dataMap.put(SINK_KEY, JsonUtils.toJson(getSink()));
        dataMap.put(TRANSFORMATION_KEY, TransformHelper.serializeTransformations(getTransformations()));
        dataMap.put(REWRITER_KEY, TransformHelper.serializeRewriters(getRewriter()));
        return JsonUtils.toJson(dataMap);
    }

    @Override
    public void deserialize(String jsonMap) {
        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> dataMap = JsonUtils.fromJson(jsonMap, mapType);

        String jidKey = dataMap.get(JID_KEY);
        this.jId = Integer.valueOf(jidKey);

        String sourceStr = dataMap.get(SOURCE_KEY);
        this.source = JsonUtils.fromJson(sourceStr, getSourceClass());

        String sinkStr = dataMap.get(SINK_KEY);
        this.sink = JsonUtils.fromJson(sinkStr, getSinkClass());

        String transformationsStr = dataMap.get(TRANSFORMATION_KEY);
        this.transformations = TransformHelper.deserializeTransformations(transformationsStr);

        String rewirterStr = dataMap.get(REWRITER_KEY);
        this.rewriter = TransformHelper.deserializeRewriters(rewirterStr);
    }

    public abstract Class<? extends Source> getSourceClass();
    public abstract Class<? extends Sink> getSinkClass();
    public abstract Streamer newStreamer();

    public Source getSource() {
        return source;
    }

    public Sink getSink() {
        return sink;
    }

    public List<Transformation> getTransformations() {
        return transformations;
    }

    public Rewriter getRewriter() {
        return rewriter;
    }

    public void setRewriter(Rewriter rewriter) {
        this.rewriter = rewriter;
    }

    public void setTransformations(List<Transformation> transformations) {
        this.transformations = transformations;
    }
}
