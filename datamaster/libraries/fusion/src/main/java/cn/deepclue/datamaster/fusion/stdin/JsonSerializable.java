package cn.deepclue.datamaster.fusion.stdin;

import cn.deepclue.datamaster.streamer.util.JsonUtils;

/**
 * Created by xuzb on 18/05/2017.
 */
public class JsonSerializable {
    public String toJson() {
        return JsonUtils.toJson(this);
    }
}
