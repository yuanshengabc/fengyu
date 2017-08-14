package cn.deepclue.datamaster.testkstreams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.StateStoreSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;

/**
 * Created by xuzb on 24/03/2017.
 */
public class Main {
    public static void main(String[] args) {
        Properties settings = new Properties();
        // Set a few key parameters
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-first-streams-application1");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "lc16:9092");
        settings.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "lc16:2181");
        // Specify default (de)serializers for record keys and for record values.
        settings.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        settings.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsConfig config = new StreamsConfig(settings);

        TopologyBuilder builder = new TopologyBuilder();

        StateStoreSupplier countStore = Stores.create("Counts")
                .withKeys(Serdes.String())
                .withValues(Serdes.Long())
                .persistent()
                .build();

        // add the source processor node that takes Kafka topic "source-topic" as input
        builder.addSource("Source", "source-topic1")

                // add the WordCountProcessor node which takes the source processor as its upstream processor
                .addProcessor("Process", ToLowerProcessor::new, "Source")

                        // create the countStore associated with the WordCountProcessor processor
                .addStateStore(countStore, "Process")

                        // add the sink processor node that takes Kafka topic "sink-topic" as output
                        // and the WordCountProcessor node as its upstream processor
                .addSink("Sink", "sink-topic1", "Process");

        KafkaStreams streams = new KafkaStreams(builder, config);

        streams.start();
    }
}
