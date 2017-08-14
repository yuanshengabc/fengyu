package cn.deepclue.demo.app.common.log;

import cn.deepclue.demo.app.common.RequestContextHolder;
import cn.deepclue.demo.app.common.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 拦截@ResponseBody响应
 * <p>
 * Created by luoyong on 17-3-29.
 */
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
        Object wrapperBody = body;
        try {
            if (!(body instanceof Response)) {
                if (body instanceof String) {
                    wrapperBody = objectMapper.writeValueAsString(new Response<>(body));
                } else {
                    wrapperBody = new Response<>(body);
                }
            }

            if (returnType.getMethodAnnotation(HttpLogIgnore.class) == null) {
                logger.info("[{}]-[{}]-[{}]", RequestContextHolder.getTraceId(), request.getURI().getPath(), body == null ? null : objectMapper.writeValueAsString(wrapperBody));
            }
        } catch (Exception e) {
            logger.error("request uri path: {}, 打印response body 出错", request.getURI().getPath(), e);
        }

        return wrapperBody;
    }

}
