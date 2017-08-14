package cn.deepclue.datamaster.streamer.util;

import cn.deepclue.datamaster.streamer.transform.filter.Filter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Json转换工具类
 *
 * @author luoyong
 * @version 1.0.0
 */
public abstract class JsonUtils {

    /**
     * Java对象转换为Json串
     *
     * @param obj Java对象
     * @return Json串
     */
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    /**
     * Json串转换为Java对象
     *
     * @param json Json串
     * @param type Java对象类型
     * @return Java对象
     */
    public static <T> T fromJson(String json, Class<T> type) {
        return new Gson().fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }

    public static String serialize(Object object, Type type, TypeAdapter typeAdapter) {
        Gson gson = new GsonBuilder().registerTypeAdapter(typeAdapter.getType(), typeAdapter).create();
        return gson.toJson(object, type);
    }

    public static <T> T deserialize(String json, Type type, TypeAdapter typeAdapter) {
        Gson gson = new GsonBuilder().registerTypeAdapter(typeAdapter.getType(),
                typeAdapter.getType()).create();
        return gson.fromJson(json, type);
    }

    public static <T> List<T> deserializeList(String json, Type listType, TypeAdapter typeAdapter) {
        Gson gson = new GsonBuilder().registerTypeAdapter(typeAdapter.getType(), typeAdapter).create();
        return gson.fromJson(json, listType);
    }
}
