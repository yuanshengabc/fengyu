package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by xuzb on 18/04/2017.
 */
public class ReplaceFieldTFTest {
    @Test
    public void transform() {
        SingleFieldTF tf = new ReplaceFieldTF("f1", "cdf");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "abc", "cdf");
        SingleFieldTFTester.test(tf, BaseType.TEXT, null, "cdf");
    }
}
