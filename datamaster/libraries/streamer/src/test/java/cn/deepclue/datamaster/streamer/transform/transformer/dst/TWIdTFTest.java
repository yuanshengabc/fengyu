package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.transformer.common.SingleFieldTFTester;
import org.junit.Test;

/**
 * Created by yuansheng on 6/21/17.
 */
public class TWIdTFTest {
    @Test
    public void transform() throws Exception {
        //去空格
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, " B14 85  23698 ", "B148523698");
        //假号码
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, "A12345678", "");
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, "B111111111", "");
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, "C987654321", "");
        //小写转大写
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, "ac45123654", "AC45123654");

        //其他情况
        SingleFieldTFTester.test(HKIdTF.class, BaseType.TEXT, "", "");
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, "A451236547", "A451236547");
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, " 159 85 ", "15985");
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, "a124+5854163", "a124+5854163");
        SingleFieldTFTester.test(TWIdTF.class, BaseType.TEXT, "11245854163", "11245854163");
    }
}