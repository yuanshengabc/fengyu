package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

/**
 * Created by magneto on 17-4-17.
 */
public class UnifyMarrigeTFTest {
    @Test
    public void transformNotNullValue() throws Exception {
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "married", "已婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "marry", "已婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "notsingle", "已婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "mrs", "已婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "已婚", "已婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "是", "已婚");


        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "single", "未婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "unmarry", "未婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "unmarried", "未婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "miss", "未婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "未婚", "未婚");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "否", "未婚");

        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "divorce", "离异");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "divorced", "离异");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "divorcer", "离异");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "离异", "离异");

        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "widow", "丧偶");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "widower", "丧偶");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "丧偶", "丧偶");

        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "dIv Or ce", "离异");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, " divorc ed ", "离异");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "divorcer", "离异");
        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "离 异", "离异");

        SingleFieldTFTester.test(UnifyMarrigeTF.class, BaseType.TEXT, "AB C", "AB C");
    }

}
