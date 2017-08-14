package cn.deepclue.common;

import cn.deepclue.common.log.LogRequestInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by luoyong on 17-7-10.
 */
@Configuration
@ComponentScan("cn.deepclue.common")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogRequestInterceptor(objectMapper)).addPathPatterns("/**");
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        return validatorFactoryBean;
    }
}
