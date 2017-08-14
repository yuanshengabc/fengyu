package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.testio.base.IOStreamerBase;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IOStreamKafka2HDFS extends IOStreamerBase {

    public void testStart(String importTableName) {

        init(importTableName);

        mySQL2Kafka();

        kafka2HDFS();
    }
}
