package cn.deepclue.datamaster.streamer.util;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by xuzb on 16/05/2017.
 */
public class DeserAdapterHelper {
    public static <T> T deserialize(JsonElement elem, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject member = (JsonObject) elem;
        final JsonElement typeString = get(member, "type");
        final JsonElement data = get(member, "data");
        final Type actualType = typeForName(typeString);

        return context.deserialize(data, actualType);
    }

    public static <T> JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject member = new JsonObject();

        member.addProperty("type", src.getClass().getName());

        member.add("data", context.serialize(src));

        return member;
    }

    private static Type typeForName(final JsonElement typeElem) {
        String typeName = typeElem.getAsString();
        try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown filter type: " + typeName, e);
        }
    }

    private static JsonElement get(final JsonObject wrapper, final String memberName) {
        final JsonElement elem = wrapper.get(memberName);

        if (elem == null) {
            throw new JsonParseException(
                    "no '" + memberName + "' member found in json string.");
        }
        return elem;
    }
}
