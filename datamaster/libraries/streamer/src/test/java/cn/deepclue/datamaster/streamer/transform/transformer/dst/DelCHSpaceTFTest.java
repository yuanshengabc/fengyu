package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.dst.DelCHSpaceTF;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

/**
 * Created by yuansheng on 17-6-19.
 */
public class DelCHSpaceTFTest {
    @Test
    public void transform() throws Exception {
        //有中文
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "  张三 ", "张三");
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "张 三", "张三");
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "这  是一 句   话", "这是一句话");
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "I  an 一个 man", "Ian一个man");
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, " a  b  一  c  d ", "ab一cd");
        //无中文
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "", "");
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "Hello Word", "Hello Word");
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "Hello  +   Word", "Hello + Word");
        SingleFieldTFTester.test(DelCHSpaceTF.class, BaseType.TEXT, "This  is   a Sentence.", "This is a Sentence.");
    }

}