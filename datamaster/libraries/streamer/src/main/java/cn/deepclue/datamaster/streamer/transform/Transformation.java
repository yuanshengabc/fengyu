package cn.deepclue.datamaster.streamer.transform;

import cn.deepclue.datamaster.streamer.transform.filter.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuzb on 05/04/2017.
 */
public class Transformation {
    private String tftype;
    private Map<String, Object> args;
    private List<Filter> filters;

    public String getTftype() {
        return tftype;
    }

    public void setTftype(String tftype) {
        this.tftype = tftype;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void addArg(String name, Object value) {
        if (args == null) {
            args = new HashMap<>();
        }

        args.put(name, value);
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public void addFilter(Filter filter) {
        if (filters == null) {
            filters = new ArrayList<>();
        }

        filters.add(filter);
    }


}
