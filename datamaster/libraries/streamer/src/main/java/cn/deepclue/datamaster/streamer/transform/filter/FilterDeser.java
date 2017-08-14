package cn.deepclue.datamaster.streamer.transform.filter;

import cn.deepclue.datamaster.streamer.util.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xuzb on 06/04/2017.
 */
public class FilterDeser {

    private FilterDeser() {}

    public static List<Filter> fromJsonList(String filtersString) {
        Type listType = new TypeToken<List<Filter>>(){}.getType();
        return JsonUtils.deserializeList(filtersString, listType,
                new FilterDeserAdapter());
    }

    public static String toJson(List<Filter> filters) {
        Type listType = new TypeToken<List<Filter>>() {}.getType();
        return JsonUtils.serialize(filters, listType, new FilterDeserAdapter());
    }
}
