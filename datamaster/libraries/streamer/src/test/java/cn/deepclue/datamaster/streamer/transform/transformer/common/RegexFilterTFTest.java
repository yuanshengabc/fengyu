package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by xuzb on 17/04/2017.
 */
public class RegexFilterTFTest {
    @Test
    public void transform() {
        SingleFieldTF tf = new RegexFilterTF("f1") {
            @Override
            protected String regex() {
                return "\\d+";
            }

            @Override
            protected String preTransformNotNullValue(String value) {
                return value;
            }

            @Override
            protected String transformCandidateValue(String candidateValue) {
                return candidateValue;
            }
        };
        SingleFieldTFTester.test(tf, BaseType.TEXT, "123", "123");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "123c", "123c");
    }
}
