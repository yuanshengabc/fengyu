package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yuansheng on 17-6-15.
 */
public class HKIdTFTest {
    @Test
    public void transform() throws Exception {
        //去空格
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, " S 14 852 3 ", "S148523");
        //假id
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "A123456(A)", "");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "C654321(A)", "");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "A111111(A)", "");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "1", "");
        //小写转大写
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "c978514", "C978514");
        //8位id
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "CW978514", "CW978514");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "C9785147", "C978514(7)");
        //9位id
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "qC9785147", "QC978514(7)");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "q97851474", "Q978514(74)");
        //10位id
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "qq97851474", "QQ978514(74)");
        //其他情况
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "", "");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "159", "159");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "1235854", "1235854");
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "qq978541474", "qq978541474");
    }

}