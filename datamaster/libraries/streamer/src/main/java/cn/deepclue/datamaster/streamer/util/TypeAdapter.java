package cn.deepclue.datamaster.streamer.util;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by xuzb on 17/05/2017.
 */
public interface TypeAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {
    Type getType();
}
