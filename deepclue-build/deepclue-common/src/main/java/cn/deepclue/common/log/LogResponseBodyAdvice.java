package cn.deepclue.common.log;

import cn.deepclue.common.RequestContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Response 的日志信息
 */
@Order(2)
@ControllerAdvice
public class LogResponseBodyAdvice implements ResponseBodyAdvice {
    private static Logger logger = LoggerFactory.getLogger(LogResponseBodyAdvice.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (returnType.getMethodAnnotation(HttpLogIgnore.class) == null) {
                logger.info("[{}]-[{}]-[{}]", RequestContextHolder.getTraceId(), request.getURI().getPath(), body == null? null: objectMapper.writeValueAsString(body));
            }
        } catch (Exception e) {
            logger.error("request uri path: {}, 打印response body 出错", request.getURI().getPath(), e);
        }
        return body;
    }

}
