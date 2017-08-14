package cn.deepclue.datamaster.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by xuzb on 07/04/2017.
 */
@SpringBootConfiguration
@ComponentScan(basePackages = {"cn.deepclue.datamaster.scheduler"})
@EnableConfigurationProperties(SchedulerIntegTestProperties.class)
public class SchedulerIntegTestConfiguration {
    @Autowired
    private SchedulerIntegTestProperties schedulerProperties;
}
