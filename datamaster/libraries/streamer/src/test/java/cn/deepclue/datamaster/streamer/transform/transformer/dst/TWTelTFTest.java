package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yuansheng on 6/21/17.
 */
public class TWTelTFTest {
    @Test
    public void transform() throws Exception {
        //去空格
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, " 775  56622 ", "88677556622");
        //清洗无关字符
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "+456852456", "886456852456");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "5(28）7_4-63+1", "88652874631");
        //去分机号
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "412352121#102", "886412352121");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "#14526398", "");
        //假号码
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "123456", "");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "12345678", "");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "1111222", "");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "987654321", "");
        //8-10位
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "89654123", "88689654123");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "896541235", "886896541235");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "8965412341", "8868965412341");
        //其他情况
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "", "");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "1", "1");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "523641", "523641");
        SingleFieldTFTester.test(TWTelTF.class, BaseType.TEXT, "88652325641", "88652325641");
    }
}