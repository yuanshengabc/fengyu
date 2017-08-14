package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.testio.base.IOStreamerBase;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/12.
 */
@Configuration
public class IOStreamerParallel extends IOStreamerBase {

    public IOStreamerParallel(){}

    public void testStart(String importTableName){

        init(importTableName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mySQL2Kafka();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                kafka2ES();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                kafka2HDFS();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                kafkaStreams();
                kafka2MySQL();
            }
        }).start();
    }
}