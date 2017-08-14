package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

/**
 * Created by xuzb on 19/04/2017.
 */
public class EmailTFTest {
    @Test
    public void transform() {
        //empty string
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "", "");
        //empty string after "@"
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email@", "email@");
        //uppercase
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "EMAIL@ERE.COM", "email@ere.com");
        //white space
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "em ail@e re.c om", "email@ere.com");
        //full width white space
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "em　ail@e　re.c　om", "email@ere.com");
        //last "."
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email@ere.com.", "email@ere.com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email@ere.com..", "email@ere.com");
        //has "@"
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email@.ere.com", "email@ere.com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email.@ere.com", "email.@ere.com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email@ere..com", "email@ere.com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "em..ail@ere.com", "em..ail@ere.com");
        //has more than one "@"
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email@.@.ere.com", "email@.@ere.com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email@ere..com@ere..com", "email@ere..com@ere.com");
        //no "@"
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email.ere.com", "email.ere.com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "email..ere.com", "email..ere.com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "emailere..com", "emailere..com");
        SingleFieldTFTester.test(EmailTF.class, BaseType.TEXT, "em..ailere..com", "em..ailere..com");
    }
}
