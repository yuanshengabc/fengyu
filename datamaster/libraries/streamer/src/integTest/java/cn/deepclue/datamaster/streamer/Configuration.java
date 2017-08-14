package cn.deepclue.datamaster.streamer;

import cn.deepclue.datamaster.streamer.io.kafka.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * 用于向KafkaUtils中注入对象信息
 * Created by ggchangan on 17-7-13.
 */
@TestConfiguration
public class Configuration {

    @Bean
    public InitStaticClassProperty initStaticClassProperty(){
        return new InitStaticClassProperty();
    }
    public static class InitStaticClassProperty{
        @Autowired
        private StreamerIntegTestProperties properties;

        @PostConstruct
        private void init(){
            Env.properties =properties;
        }

    }


}
