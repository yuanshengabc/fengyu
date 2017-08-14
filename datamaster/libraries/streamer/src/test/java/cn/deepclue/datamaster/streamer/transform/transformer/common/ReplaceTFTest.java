package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import org.junit.Test;

/**
 * Created by xuzb on 18/04/2017.
 */
public class ReplaceTFTest {
    @Test
    public void transform() {
        ReplaceTF tf = new ReplaceTF("f1", "abc", "cdf");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "abcdef", "cdfdef");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "abcdefabc", "cdfdefcdf");
    }
}
