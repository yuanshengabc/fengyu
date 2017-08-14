package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.testio.base.IOStreamerBase;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/20.
 */
@Configuration
public class IOStreamSerial extends IOStreamerBase {

    public void testStart(String importTableName) {
        init(importTableName);
        mySQL2Kafka();
        kafka2ES();
        kafka2HDFS();
        kafkaStreams();
        kafka2MySQL();
    }
}
