package cn.deepclue.datamaster.kconnector.main;

import cn.deepclue.datamaster.streamer.config.kafka.Worker;
import cn.deepclue.datamaster.streamer.config.kafka.sink.KafkaSink;
import cn.deepclue.datamaster.streamer.config.kafka.source.MySQLConnectorSource;
import cn.deepclue.datamaster.streamer.impl.kconnector.MySQL2KafkaStreamer;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        MySQL2KafkaStreamer sql2KafkaStreamer=new MySQL2KafkaStreamer();

        MySQLConnectorSource source=new MySQLConnectorSource("mysqlsource","jdbc:mariadb://172.24.63.40/mytest?user=root&password=root","id");
        KafkaSink sink=new KafkaSink("si_","localhost:9092");//
        Worker woker=new Worker("localhost:9092");

        sql2KafkaStreamer.addSource(source);
        sql2KafkaStreamer.addSink(sink);

        sql2KafkaStreamer.start();
    }

    public Config parseConfig() {
        StringBuilder str = new StringBuilder();
        String line;
        try {
            File file = new File("test/kconnector/config.json");
            try (
                    FileReader fileReader = new FileReader(file)) {
                BufferedReader br = new BufferedReader(fileReader);
                while ((line = br.readLine()) != null) {
                    str.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(str.toString(), Config.class);
    }
}
