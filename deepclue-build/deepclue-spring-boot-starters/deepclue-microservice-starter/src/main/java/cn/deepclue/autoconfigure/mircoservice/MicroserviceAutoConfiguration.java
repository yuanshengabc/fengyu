package cn.deepclue.autoconfigure.mircoservice;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by luoyong on 17-7-13.
 */
@Configuration
@Import({MicroserviceAutoConfiguration.MicroserviceConfiguration.class})
public class MicroserviceAutoConfiguration {

    @Configuration
    @EnableDiscoveryClient
    @RefreshScope
    public static class MicroserviceConfiguration{
    }
}
