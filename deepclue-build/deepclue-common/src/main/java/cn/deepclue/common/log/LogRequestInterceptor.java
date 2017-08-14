package cn.deepclue.common.log;

import cn.deepclue.common.RequestContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 请求的 URL 和 参数 的日志信息
 */
public class LogRequestInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LogRequestInterceptor.class);

    private ObjectMapper objectMapper;

    public LogRequestInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = UUID.randomUUID().toString();
        RequestContextHolder.setTraceId(traceId);
        if (request.getParameterMap().isEmpty()) {
            logger.info("[{}]-[{}]", RequestContextHolder.getTraceId(), request.getRequestURI());
        } else {
            logger.info("[{}]-[{}]-[{}]", RequestContextHolder.getTraceId(), request.getRequestURI(), objectMapper.writeValueAsString(request.getParameterMap()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContextHolder.reset();
    }
}
