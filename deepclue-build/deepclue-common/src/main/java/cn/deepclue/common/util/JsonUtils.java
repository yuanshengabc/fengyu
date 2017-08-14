package cn.deepclue.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * json字符串与java对象转换工具类
 *
 */
public abstract class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将java对象转成json
     */
    public static String toJson(Object obj) {
        if (obj == null || obj instanceof String) {
            return (String) obj;
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("将Java对象转换成json串出错！", e);
            throw new JsonConvertException("将Java对象转换成Json串出错！", e);
        }
    }

    /**
     * 将json 转换成java对象
     */
    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            logger.error("json串转换成对象出错, json：{}, type: {}", json, type);
            throw new JsonConvertException("Json串转换成对象出错!", e);
        }
    }

    /**
     * 将json转换成java泛型对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            logger.error("Json串转换成对象出错：{}, typeRef: {}", json, typeRef);
            throw new JsonConvertException("Json串转换成对象出错!", e);
        }
    }

    public static class JsonConvertException extends RuntimeException {
        public JsonConvertException() {
        }

        public JsonConvertException(String message) {
            super(message);
        }

        public JsonConvertException(String message, Throwable cause) {
            super(message, cause);
        }

        public JsonConvertException(Throwable cause) {
            super(cause);
        }

        public JsonConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
