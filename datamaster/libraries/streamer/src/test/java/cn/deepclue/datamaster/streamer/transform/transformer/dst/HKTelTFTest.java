package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

/**
 * Created by xuzb on 18/04/2017.
 */
public class HKTelTFTest {

    private void testHKTelTF(String source, String expected) {
        SingleFieldTFTester.test(HKTelTF.class, BaseType.TEXT, source, expected);
    }

    @Test
    public void transform() {
        // test del space
        testHKTelTF("111232 (33) -3453 ", "85211123233");

        // test normal tel str
        testHKTelTF("11121221", null);
        testHKTelTF("11123233", "85211123233");
        testHKTelTF("411232334", "852411232334");
        testHKTelTF("85299999999", null);

        // test str with  "(",")","+","-"
        testHKTelTF("+11123233", "85211123233");
        testHKTelTF("+88611123233", "88611123233");
        testHKTelTF("+111-2323-3", "85211123233");
        testHKTelTF("11123233-3453", "85211123233");
        testHKTelTF("11121-221-223", null);
        testHKTelTF("111232(33)-3453", "85211123233");
        testHKTelTF("11123233(435", "85211123233");
        testHKTelTF("111-23233(435", "85211123233");
        testHKTelTF("+82++++56-47(88)", "85282564788");
        testHKTelTF("+82++++6-47(88)", "8264788");
        testHKTelTF("+852123-456-47(88)", "85212345647");
        testHKTelTF("+82(21)23-456-47(88)", "82212345647");
        testHKTelTF("1112(12)21", null);
        testHKTelTF("+852998673411", "+852998673411");

        // test drop the str which not fit the data of phone
        testHKTelTF("11123ads233", "85211123233");
        testHKTelTF("1112#*3==233", "85211123233");
        testHKTelTF("11@12!3ad233", "85211123233");

        //test filter fake number
        testHKTelTF("123456", null);
        testHKTelTF("98765432", null);
        testHKTelTF("12345678", null);
        testHKTelTF("111111", null);
    }
}
