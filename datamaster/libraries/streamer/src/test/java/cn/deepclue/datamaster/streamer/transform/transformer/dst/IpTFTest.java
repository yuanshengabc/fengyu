package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

/**
 * Created by magneto on 17-4-17.
 */
public class IpTFTest {
    @Test
    public void transformNotNullValue() {
        //empty string
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "", "");
        //first "."
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, ".1.1.1.1", "1.1.1.1");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "..1.1.1.1", "1.1.1.1");
        //last "."
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "1.1.1.1.", "1.1.1.1");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "1.1.1.1..", "1.1.1.1");
        //white space
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, " 1 . 1 . 1 . 1 ", "1.1.1.1");
        //full width white space
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "　1　.　1　.　1　.　1　", "1.1.1.1");
        //0-255.0-255.0-255.0-255, contain 0-255.0-255.0-255
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "01.01.01.01", "1.1.1.1");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "10.10.10.10", "10.10.10.10");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "01.01.01", "1.1.1");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "10.10.10", "10.10.10");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "0010.0010.0010.0010", "10.10.10.10");
        //not 0-255.0-255.0-255.0-255
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "01.01", "01.01");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "01.01.01.01.01", "01.01.01.01.01");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "01,01,01,01", "01,01,01,01");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "01a.01a.01a.01a", "01a.01a.01a.01a");
        SingleFieldTFTester.test(IpTF.class, BaseType.TEXT, "256.01.01.01", "256.01.01.01");
    }

}
