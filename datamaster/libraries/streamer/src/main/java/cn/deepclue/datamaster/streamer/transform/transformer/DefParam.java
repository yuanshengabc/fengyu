package cn.deepclue.datamaster.streamer.transform.transformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuzb on 10/04/2017.
 */
public class DefParam {
    private Map<String, Object> params;

    public DefParam() {
        params = new HashMap<>();
    }

    public void addParamPair(String name, Object value) {
        params.put(name, value);
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
