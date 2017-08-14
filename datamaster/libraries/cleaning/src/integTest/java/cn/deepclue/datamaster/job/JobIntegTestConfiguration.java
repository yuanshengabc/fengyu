package cn.deepclue.datamaster.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by xuzb on 07/04/2017.
 */
@SpringBootConfiguration
@ComponentScan(basePackages = {"cn.deepclue.datamaster.job"})
@EnableConfigurationProperties(JobIntegTestProperties.class)
public class JobIntegTestConfiguration {
    @Autowired
    private JobIntegTestProperties schedulerProperties;
}
