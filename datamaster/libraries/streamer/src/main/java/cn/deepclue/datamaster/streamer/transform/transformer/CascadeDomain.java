package cn.deepclue.datamaster.streamer.transform.transformer;

import java.util.List;
import java.util.Map;

/**
 * Created by xuzb on 11/05/2017.
 */
public class CascadeDomain {
    private String paramName;
    private Map<String, Object> typeMappings;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Map<String, Object> getTypeMappings() {
        return typeMappings;
    }

    public void setTypeMappings(Map<String, Object> typeMappings) {
        this.typeMappings = typeMappings;
    }
}
