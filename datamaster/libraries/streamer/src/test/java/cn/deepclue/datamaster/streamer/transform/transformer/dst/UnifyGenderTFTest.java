package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

/**
 * Created by magneto on 17-4-17.
 */
public class UnifyGenderTFTest {
    @Test
    public void transformNotNullValue() throws Exception {
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "m", "男");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "male", "男");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "mr", "男");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "先生", "男");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "男士", "男");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "男", "男");

        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "f", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "female", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "ms", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "miss", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "mrs", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "mstr", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "madam", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "mdm", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "女士", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "小姐", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "太太", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "夫人", "女");

        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "ma Dam", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, " md M", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "女 士", "女");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, " 小 姐 ", "女");

        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, " ABC ", " ABC ");
        SingleFieldTFTester.test(UnifyGenderTF.class, BaseType.TEXT, "ABC ", "ABC ");
    }

}
