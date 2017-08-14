package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by magneto on 17-4-17.
 */
public class RetainAlphabetTFTest {
    @Test
    public void transformNotNullValue() throws Exception {
        SingleFieldTF tf = new RetainAlphabetTF("f1");
        String[][] expects = {
                {"", ""},
                {"ABC", "ABC"},
                {"1A2BC3", "ABC"},
                {"12C3", "C"},
                {"123", ""},
                {"C123", "C"},
                {"123C", "C"},
                {"C123C", "CC"},
                {"abc", "abc"},
                {"1A2bC3", "AbC"},
                {"NULL", ""},
                {"成圣展", ""},
                {"aa成圣展bb", "aabb"},
                {"aB成圣展Cb", "aBCb"}
        };

        for (int i=0; i<expects.length; i++) {
            SingleFieldTFTester.test(tf, BaseType.TEXT, expects[i][0], expects[i][1]);
        }
    }
}
