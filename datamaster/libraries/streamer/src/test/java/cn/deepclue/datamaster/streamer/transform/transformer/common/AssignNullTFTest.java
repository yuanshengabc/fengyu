package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by xuzb on 21/04/2017.
 */
public class AssignNullTFTest {
    @Test
    public void transform() {
        SingleFieldTF tf = new AssignNullTF("f1");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", null);
    }
}
