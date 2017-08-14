package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import org.junit.Test;

/**
 * Created by xuzb on 14/04/2017.
 */
public class ToLowerTFTest {
    @Test
    public void transform() {
        SingleFieldTFTester.test(ToLowerTF.class, BaseType.TEXT, "ABc", "abc");
    }
}
