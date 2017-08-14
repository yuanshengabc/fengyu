package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by magneto on 17-4-17.
 */
public class TruncateTFTest {
    @Test
    public void transform() {
        SingleFieldTF tf = new TruncateTF("f1", 0, true);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "");
        tf = new TruncateTF("f1", 2, true);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "AB");
        tf = new TruncateTF("f1", 3, true);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "ABC");
        tf = new TruncateTF("f1", 5, true);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "ABC");

        tf = new TruncateTF("f1", 0, false);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "");
        tf = new TruncateTF("f1", 2, false);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "BC");
        tf = new TruncateTF("f1", 3, false);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "ABC");
        tf = new TruncateTF("f1", 5, false);
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "ABC");
    }
}
