package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.testio.base.IOStreamerBase;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/20.
 */
@Configuration
public class IOStreamKafka2ES extends IOStreamerBase {

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
                kafka2ES();
            }
        }).start();
    }
}
