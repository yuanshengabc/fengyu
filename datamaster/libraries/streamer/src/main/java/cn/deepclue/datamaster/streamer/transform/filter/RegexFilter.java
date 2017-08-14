package cn.deepclue.datamaster.streamer.transform.filter;

import cn.deepclue.datamaster.model.schema.RSSchema;

import java.util.regex.Pattern;

/**
 * Created by xuzb on 27/05/2017.
 */
public class RegexFilter extends SingleFieldFilter {

    private String regex;
    private Boolean neg;

    private Pattern pattern;

    public RegexFilter(String sourceFieldName, String regex, Boolean neg) {
        super(sourceFieldName);
        this.regex = regex;
        this.neg = neg;
    }

    @Override
    public void prepareSchema(RSSchema schema) {
        super.prepareSchema(schema);

        pattern = Pattern.compile(regex);
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isNeg() {
        return neg != null && neg;
    }

    public void setNeg(boolean neg) {
        this.neg = neg;
    }

    @Override
    protected boolean acceptValue(Object value) {
        String strValue = value.toString();

        boolean result = pattern.matcher(strValue).matches();

        if (neg == null || !neg) {
            return result;
        } else {
            return !result;
        }
    }
}
