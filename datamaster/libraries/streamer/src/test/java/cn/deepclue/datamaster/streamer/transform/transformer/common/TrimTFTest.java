package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by magneto on 17-4-15.
 */
public class TrimTFTest {
    @Test
    public void transform() {
        SingleFieldTF tf = new TrimTF("f1", false);
        SingleFieldTFTester.test(tf, BaseType.TEXT, " ABC ", "ABC");
        SingleFieldTFTester.test(tf, BaseType.TEXT, " A B C ", "A B C");

        tf = new TrimTF("f1", true);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "ABC");
        SingleFieldTFTester.test(tf, BaseType.TEXT, " ABC ", "ABC");
        SingleFieldTFTester.test(tf, BaseType.TEXT, " A B C ", "ABC");
    }
}
