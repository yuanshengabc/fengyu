package cn.deepclue.datamaster.cleaner.config;

import cn.deepclue.datamaster.streamer.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * Created by luoyong on 17-3-29.
 */
@ControllerAdvice
public class LogRequestBodyAdvice extends RequestBodyAdviceAdapter {
    private static Logger logger = LoggerFactory.getLogger(LogRequestBodyAdvice.class);


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        logger.info("[{}]-[{}]", RequestContextHolder.getTraceId(), JsonUtils.toJson(body));
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
