package cn.deepclue.demo.app.common.log;

import cn.deepclue.demo.app.common.RequestContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 拦截query string 参数
 *
 * Created by luoyong on 17-3-28.
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContextHolder.reset();
    }
}
