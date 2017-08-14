package cn.deepclue.datamaster.cleaner.domain.vo.data;

import cn.deepclue.datamaster.model.schema.RSSchema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuzb on 11/05/2017.
 */
public class RecordVO {
    private List<Object> values = new ArrayList<>();
    private RSSchema schema;

    public void addValue(Object value) {
        if (value != null && value instanceof Date) {
            values.add(((Date) value).getTime());
        } else {
            values.add(value);
        }
    }

    public List<Object> getValues() {
        return values;
    }

    public RSSchema getSchema() {
        return schema;
    }

    public void setSchema(RSSchema schema) {
        this.schema = schema;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
