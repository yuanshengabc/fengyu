package cn.deepclue.datamaster.testkstreams;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

/**
 * Created by xuzb on 24/03/2017.
 */
public class ToLowerProcessor implements Processor<String, String> {

    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.context.schedule(10000);
    }

    @Override
    public void process(String key, String value) {
        value = value.toLowerCase();
        System.out.println(value);
        this.context.forward(key, value);
    }

    @Override
    public void punctuate(long timestamp) {
        this.context.commit();
    }

    @Override
    public void close() {

    }
}
