package cn.deepclue.demo.appb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@RefreshScope
public class AppBApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppBApplication.class, args);
	}

	@Bean
	public AlwaysSampler defaultSampler(){
		return new AlwaysSampler();
	}

}
