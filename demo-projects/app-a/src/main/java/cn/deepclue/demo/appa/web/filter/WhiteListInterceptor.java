package cn.deepclue.demo.appa.web.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class WhiteListInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(WhiteListInterceptor.class);

    private String ipString;
    private String[] whiteList;

    public WhiteListInterceptor(String whiteList){
        this.ipString=whiteList;
        this.whiteList=whiteList.split(",");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!Arrays.asList(whiteList).contains(getIp(request))){
            logger.info("WhiteList of app-a:[{}],Request ip:[{}],The request ip is not credible!",ipString,getIp(request));
            response.setStatus(401);
            return false;
        }
        logger.info("WhiteList of app-a:[{}],Request ip:[{}]",ipString,getIp(request));
        return true;
    }

    private String getIp(HttpServletRequest request) {

        // Multilayer agent
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                // Many reverse proxy will have multiple ip, the first one is the real
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        // no agent
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
}