package cn.deepclue.datamaster.fusion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by xuzb on 23/05/2017.
 */
@SpringBootConfiguration
@ComponentScan(basePackages = {"cn.deepclue.datamaster.fusion"})
@EnableConfigurationProperties(FusionIntegTestProperties.class)
public class FusionIntegTestConfiguration {

    @Autowired
    private FusionIntegTestProperties properties;
}
