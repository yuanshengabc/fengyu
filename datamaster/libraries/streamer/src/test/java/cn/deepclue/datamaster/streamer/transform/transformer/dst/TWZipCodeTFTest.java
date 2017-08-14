package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yuansheng on 6/21/17.
 */
public class TWZipCodeTFTest {
    @Test
    public void transform() throws Exception {
        //空值
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "", "");
        //去空格
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, " 4 3 0 0 0 0 ", "430000");
        //假邮编
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "123456", "");
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "987654", "");
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "111111", "");
        //位数大于6或小于2
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "1", "");
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "7485963", "");
        //位数大于1且小于7
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "30", "30");
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "124", "124");
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "9631", "9631");
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "45652", "45652");
        SingleFieldTFTester.test(TWZipCodeTF.class, BaseType.TEXT, "456852", "456852");
    }
}