package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.Writer;
import cn.deepclue.datamaster.streamer.transform.Rewriter;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 29/03/2017.
 */
public abstract class RWStreamer implements Streamer {

    private static Logger logger = LoggerFactory.getLogger(RWStreamer.class);

    private Reader reader;
    private Writer writer;
    List<Transformer> transformers = new ArrayList<>();
    private Rewriter rewriter;
    private long nProcessed = 0;

    public RWStreamer(Source source, Sink sink) {
        addSource(source);
        addSink(sink);
    }

    public RWStreamer() {
        // Empty constructor
    }

    public long getNProcessedRecords() {
        return nProcessed;
    }

    @Override
    public boolean start() {
        logger.info("start stream from {} to {}", reader, writer);

        nProcessed = 0;
        try {

            writer.open();
            reader.open();

            RSSchema targetSchema = reader.readSchema();
            for (Transformer transformer : transformers) {
                targetSchema = transformer.prepareSchema(targetSchema);
            }

            if (rewriter != null) {
                targetSchema = rewriter.rewriteSchema(targetSchema);
            }

            writer.writeSchema(targetSchema);

            Record lastRecord = null;

            while (reader.hasNext()) {
                Record record;
                try {
                    record = reader.readRecord();
                } catch (Exception e) {
                    logger.error("Failed to read record since last record {} with exception {}", lastRecord, e);
                    break;
                }

                try {
                    for (Transformer transformer : transformers) {
                        record = transformer.transform(record);
                    }

                    if (rewriter != null) {
                        record = rewriter.rewriteRecord(targetSchema, record);
                    }

                    writer.writeRecord(record);
                } catch (Exception e) {
                    logger.error("Failed to write record {} with exception {}", record, e);
                }

                nProcessed++;

                if (nProcessed % 100000 == 0) {
                    logger.info("{} records stream from {} to {}", nProcessed, reader, writer);
                }

                lastRecord = record;
            }
        } finally {
            reader.close();
            writer.close();
        }

        logger.info("{} records stream from {} to {}", nProcessed, reader, writer);

        return true;
    }

    protected abstract Reader newReader(Source source);
    protected abstract Writer newWriter(Sink sink);

    @Override
    public void addSource(Source source) {
        reader = newReader(source);
    }

    @Override
    public void addSink(Sink sink) {
        writer = newWriter(sink);
    }

    @Override
    public void registerTransform(Transformer transformer) {
        transformers.add(transformer);
    }

    public void setRewriter(Rewriter rewriter) {
        this.rewriter = rewriter;
    }

    public Reader getReader() {
        return reader;
    }

    public Writer getWriter() {
        return writer;
    }
}
