package cn.deepclue.datamaster.streamer.transform.filter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by xuzb on 06/04/2017.
 */
public class FilterDeserTest {

    @Test
    public void testDeser() {
        LongRangeFilter longRangeFilter = new LongRangeFilter("f1");
        longRangeFilter.setMaxValue(100l);
        longRangeFilter.setMinValue(1l);

        List<Filter> filters = new ArrayList<>();
        filters.add(longRangeFilter);

        String filterString = FilterDeser.toJson(filters);

        List<Filter> newFilters = FilterDeser.fromJsonList(filterString);


        assertEquals(newFilters.size(), 1);

        Filter filter = newFilters.get(0);
        assertTrue(filter instanceof LongRangeFilter);

        LongRangeFilter newFilter = (LongRangeFilter) filter;
        assertEquals(newFilter.getMaxValue(), longRangeFilter.getMaxValue());
        assertEquals(newFilter.getMinValue(), longRangeFilter.getMinValue());
        assertEquals(newFilter.getSourceFieldName(), longRangeFilter.getSourceFieldName());
    }
}
