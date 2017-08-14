package cn.deepclue.datamaster.streamer.transform.filter;

import cn.deepclue.datamaster.streamer.exception.RegexFilterException;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by xuzb on 06/04/2017.
 */
public class TextFilter extends SingleFieldFilter {

    private String text;
    private int type;
    private Pattern pattern;

    public TextFilter(String sourceFieldName, String text, MatchType matchType) {
        super(sourceFieldName);
        this.text = text;
        setMatchType(matchType);
        if (matchType == MatchType.REGEX || matchType == MatchType.REGEX_NEG) {
            try {
                pattern = Pattern.compile(text);
            } catch (PatternSyntaxException e) {
                throw new RegexFilterException(e.getMessage(), "非法正则表达式！", e);
            }
        }
    }

    @Override
    protected boolean acceptValue(Object value) {
        String strValue = value.toString();

        switch (getMatchType()) {
            case EQUALS:
                return strValue.equals(text);
            case EQUALS_IGNORE_CASE:
                return strValue.equalsIgnoreCase(text);
            case STARTS:
                return strValue.startsWith(text);
            case ENDS:
                return strValue.endsWith(text);
            case CONTAINS:
                return strValue.contains(text);
            case REGEX:
                return pattern.matcher(strValue).matches();
            case REGEX_NEG:
                return !pattern.matcher(strValue).matches();

            default:
                throw new IllegalStateException("Unknown match type");
        }
    }

    public MatchType getMatchType() {
        return MatchType.getMatchType(type);
    }

    public void setMatchType(MatchType matchType) {
        this.type = matchType.getValue();
    }

    public String getText() {
        return text;
    }

    public enum MatchType {
        EQUALS(0), STARTS(1), ENDS(2), CONTAINS(3), REGEX(4), REGEX_NEG(5), EQUALS_IGNORE_CASE(6);

        private int value;
        MatchType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static MatchType getMatchType(int value) {
            switch (value) {
                case 0:
                    return EQUALS;
                case 1:
                    return STARTS;
                case 2:
                    return ENDS;
                case 3:
                    return CONTAINS;
                case 4:
                    return REGEX;
                case 5:
                    return REGEX_NEG;
                case 6:
                    return EQUALS_IGNORE_CASE;

                default:
                    throw new IllegalStateException("Unknown match type: " + value);
            }
        }
    }
}
