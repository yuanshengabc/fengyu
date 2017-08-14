package cn.deepclue.autoconfigure.web;

import cn.deepclue.common.WebConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by jiapan on 2017/7/6.
 */
@Configuration
@ConditionalOnClass(WebConfiguration.class)
@Import({WebConfiguration.class})
public class WebAutoConfiguration {
}
