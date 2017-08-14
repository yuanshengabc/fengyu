package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import org.junit.Test;

/**
 * Created by xuzb on 14/04/2017.
 */
public class ToUpperTFTest {
    @Test
    public void transform() {
        SingleFieldTFTester.test(ToUpperTF.class, BaseType.TEXT, "ABc", "ABC");
    }
}
