package cn.deepclue.demo.app.common.log;

import cn.deepclue.demo.app.common.RequestContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * 拦截打印@RequestBody请求参数
 *
 * Created by luoyong on 17-3-29.
 */
@ControllerAdvice
public class LogRequestBodyAdvice extends RequestBodyAdviceAdapter {
    private static Logger logger = LoggerFactory.getLogger(LogRequestBodyAdvice.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            if (parameter.getMethodAnnotation(HttpLogIgnore.class) == null) {
                logger.info("[{}]-[{}]", RequestContextHolder.getTraceId(), objectMapper.writeValueAsString(body));
            }
        } catch (Exception e) {
            logger.error("request id: {}, 打印request body 出错", RequestContextHolder.getTraceId(), e);
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
