package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by magneto on 17-4-15.
 */
public class AddSuffixTFTest {
    @Test
    public void transform() {
        SingleFieldTF tf = new AddSuffixTF("f1", "_tf");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "ABC_tf");
    }
}
