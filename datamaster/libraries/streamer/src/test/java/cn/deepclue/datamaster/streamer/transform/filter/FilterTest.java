package cn.deepclue.datamaster.streamer.transform.filter;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by xuzb on 07/04/2017.
 */
public class FilterTest {

    private void testLongRangeFilter(Long maxValue, Long minValue, Long value, boolean expect) {
        LongRangeFilter longRangeFilter = new LongRangeFilter("f1");
        longRangeFilter.setMaxValue(maxValue);
        longRangeFilter.setMinValue(minValue);

        RSSchema schema = new RSSchema();
        schema.addField(new RSField("f1", BaseType.LONG));

        Record record = new Record(schema);
        record.addValue(value);

        longRangeFilter.prepareSchema(schema);

        assertEquals(longRangeFilter.accept(record), expect);
    }

    @Test
    public void testLongRangeFilter() {
        testLongRangeFilter(100l, 5l, 9l, true);
        testLongRangeFilter(100l, 5l, 101l, false);
        testLongRangeFilter(null, 5l, 9l, true);
        testLongRangeFilter(null, 5l, 3l, false);
        testLongRangeFilter(100l, null, 9l, true);
        testLongRangeFilter(100l, null, 101l, false);
    }

    private void testDoubleRangeFilter(Double maxValue, Double minValue, Double value, boolean expect) {
        DoubleRangeFilter doubleRangeFilter = new DoubleRangeFilter("f1");
        doubleRangeFilter.setMaxValue(maxValue);
        doubleRangeFilter.setMinValue(minValue);

        RSSchema schema = new RSSchema();
        schema.addField(new RSField("f1", BaseType.DOUBLE));

        Record record = new Record(schema);
        record.addValue(value);

        doubleRangeFilter.prepareSchema(schema);

        assertEquals(doubleRangeFilter.accept(record), expect);
    }

    @Test
    public void testDoubleRangeFilter() {
        testDoubleRangeFilter(100d, 5d, 9d, true);
        testDoubleRangeFilter(100d, 5d, 101d, false);
        testDoubleRangeFilter(null, 5d, 9d, true);
        testDoubleRangeFilter(null, 5d, 3d, false);
        testDoubleRangeFilter(100d, null, 9d, true);
        testDoubleRangeFilter(100d, null, 101d, false);
    }

    private void testTextFilter(String value, String text, TextFilter.MatchType matchType, boolean expect) {
        TextFilter filter = new TextFilter("f1", text, matchType);

        RSSchema schema = new RSSchema();
        schema.addField(new RSField("f1", BaseType.TEXT));

        Record record = new Record(schema);
        record.addValue(value);


        filter.prepareSchema(schema);

        assertEquals(filter.accept(record), expect);
    }

    @Test
    public void testTextFilter() {
        testTextFilter("abc", "abc", TextFilter.MatchType.EQUALS, true);
        testTextFilter("abc", "ab", TextFilter.MatchType.EQUALS, false);

        testTextFilter("abc", "abC", TextFilter.MatchType.EQUALS_IGNORE_CASE, true);
        testTextFilter("abc", "abD", TextFilter.MatchType.EQUALS_IGNORE_CASE, false);

        testTextFilter("abc", "ab", TextFilter.MatchType.STARTS, true);
        testTextFilter("abc", "bc", TextFilter.MatchType.STARTS, false);

        testTextFilter("abc", "bc", TextFilter.MatchType.ENDS, true);
        testTextFilter("abc", "ab", TextFilter.MatchType.ENDS, false);

        testTextFilter("abc", "b", TextFilter.MatchType.CONTAINS, true);
        testTextFilter("abc", "bd", TextFilter.MatchType.CONTAINS, false);

        testTextFilter("abc", "\\w+", TextFilter.MatchType.REGEX, true);
        testTextFilter("abc", "\\d+", TextFilter.MatchType.REGEX, false);

        testTextFilter("abc", "\\w+", TextFilter.MatchType.REGEX_NEG, false);
        testTextFilter("abc", "\\d+", TextFilter.MatchType.REGEX_NEG, true);
    }

    private void testNotNullFilter(Object value, BaseType baseType, boolean expect) {
        NotNullFilter filter = new NotNullFilter("f1");

        RSSchema schema = new RSSchema();
        schema.addField(new RSField("f1", baseType));

        Record record = new Record(schema);
        record.addValue(value);

        filter.prepareSchema(schema);

        assertEquals(filter.accept(record), expect);
    }

    @Test
     public void testNotNullFilter() {
        testNotNullFilter("abc", BaseType.TEXT, true);
        testNotNullFilter("", BaseType.TEXT, false);

        testNotNullFilter(1, BaseType.INT, true);
        testNotNullFilter(null, BaseType.INT, false);
    }

    private void testRegexFilter(String value, String regex, boolean neg, boolean expect) {
        Filter filter = new RegexFilter("f1", regex, neg);

        RSSchema schema = new RSSchema();
        schema.addField(new RSField("f1", BaseType.TEXT));

        Record record = new Record(schema);
        record.addValue(value);

        filter.prepareSchema(schema);

        assertEquals(filter.accept(record), expect);
    }

    @Test
    public void testRegexFilter() {
        // Test for 'neg' variable
        testRegexFilter("abc", "abc", false, true);
        testRegexFilter("abc", "abc", true, false);

        // Test for whole match
        testRegexFilter("abcd", "abc", false, false);
        testRegexFilter("abcd", "abc\\w", false, true);
    }
}
