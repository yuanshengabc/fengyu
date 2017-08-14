package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by magneto on 17-4-17.
 */
public class RetainDigitTFTest {
    @Test
    public void transformNotNullValue() throws Exception {
        SingleFieldTF tf = new RetainDigitTF("f1");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "", "");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "1A2BC3", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "12C3", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "123", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "C123", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "123C", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "C123C", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "C123C中国", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "22C123C中国33", "2212333");
    }

}
