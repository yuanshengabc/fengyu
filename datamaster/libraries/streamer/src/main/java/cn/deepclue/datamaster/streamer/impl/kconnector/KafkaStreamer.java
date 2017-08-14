package cn.deepclue.datamaster.streamer.impl.kconnector;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.KafkaConnector;
import cn.deepclue.datamaster.streamer.config.kafka.Worker;
import cn.deepclue.datamaster.streamer.config.kafka.sink.KafkaSink;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.KafkaSource;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.exception.KafkaException;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class KafkaStreamer implements Streamer {
    private Source source;
    private Sink sink;

    private String kafkaBinPath = "/opt/confluent/bin/connect-standalone";

    /**
     * Start the task of source or sink.
     *
     * @return Whether the task is finished successfully.
     */
    @Override
    public boolean start() {
        if (source == null || sink == null) {
            return false;
        }

        String workerPath;
        String connectorPath;

        if (source instanceof KafkaConnector && sink instanceof KafkaSink) {
            workerPath = getTempFile(new Worker(((KafkaSink) sink).getBootstrapServers()).toWorkerString());
            connectorPath = getTempFile(((KafkaConnector) source).toPropertyString() + "\n" + ((KafkaSink) sink).getTopicPrefix());
        } else if (sink instanceof KafkaConnector && source instanceof KafkaSource) {
            workerPath = getTempFile(new Worker(((KafkaSource) source).getBootstrapServers()).toWorkerString());
            connectorPath = getTempFile(((KafkaSource) source).getTopics() + "\n" + ((KafkaConnector) sink).toPropertyString());
        } else
            return false;

        execute(kafkaBinPath + " " + workerPath + " " + connectorPath);
        return true;
    }

    /**
     * Set source.
     *
     * @param source The source of this task.
     */
    @Override
    public void addSource(Source source) {
        this.source = source;
    }

    /**
     * Set sink
     *
     * @param sink The sink of this task.
     */
    @Override
    public void addSink(Sink sink) {
        this.sink = sink;
    }

    @Override
    public void registerTransform(Transformer transformer) {

    }

    public void setKafkaBinPath(String kafkaBinPath) {
        this.kafkaBinPath = kafkaBinPath;
    }

    public Source getSource() {
        return source;
    }

    public Sink getSink() {
        return sink;
    }

    /**
     * Execute the command.
     *
     * @param cmd The command which will be executed.
     */
    private void execute(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);

        } catch (IOException e) {
            throw new KafkaException("Kafka IOException", "Kafka输入输出异常", e);
        }
    }

    /**
     * Generate the file of properties.
     *
     * @param content The content of properties.
     * @return The path of the properties file.
     */
    public String getTempFile(String content) {
        try {
            File dir = new File("/tmp/kafka-connect");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = File.createTempFile("connect", ".properties", new File("/tmp/kafka-connect"));
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(content.getBytes());
                out.close();
                return file.getAbsolutePath();
            } catch (IOException e) {
                throw new KafkaException("Kafka IOException", "Kafka输入输出异常", e);
            }
        } catch (IOException e) {
            throw new KafkaException("Kafka IOException", "Kafka输入输出异常", e);
        }
    }
}
