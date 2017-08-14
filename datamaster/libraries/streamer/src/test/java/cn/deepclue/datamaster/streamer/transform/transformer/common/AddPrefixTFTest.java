package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import org.junit.Test;

/**
 * Created by magneto on 17-4-15.
 */
public class AddPrefixTFTest {
    @Test
    public void transform() {
        SingleFieldTF tf = new AddPrefixTF("f1", "tf_");
        SingleFieldTFTester.test(tf, BaseType.TEXT, "ABC", "tf_ABC");
    }
}
