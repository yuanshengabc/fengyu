package cn.deepclue.datamaster.streamer.transform.filter;

import cn.deepclue.datamaster.streamer.util.*;
import cn.deepclue.datamaster.streamer.util.TypeAdapter;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by xuzb on 06/04/2017.
 */
public class FilterDeserAdapter implements TypeAdapter<Filter> {
    @Override
    public Filter deserialize(JsonElement elem, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return DeserAdapterHelper.deserialize(elem, typeOfT, context);
    }

    @Override
    public JsonElement serialize(Filter src, Type typeOfSrc, JsonSerializationContext context) {
        return DeserAdapterHelper.serialize(src, typeOfSrc, context);
    }

    @Override
    public Type getType() {
        return Filter.class;
    }
}
