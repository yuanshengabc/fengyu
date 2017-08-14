package cn.deepclue.datamaster.streamer.transform.transformer;

import cn.deepclue.datamaster.streamer.transform.transformer.common.ToLowerTF;
import cn.deepclue.datamaster.streamer.transform.transformer.common.TruncateTF;
import cn.deepclue.datamaster.streamer.transform.transformer.field.AddFieldTF;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by xuzb on 05/04/2017.
 */
public class TransformerDefTest {
    @Test
    public void testFromTransfomerClass() {
        TransformerDef td = TransformerDef.fromTransformer(ToLowerTF.class);
        assertEquals(td.getTftype(), "ToLowerTF");
        assertEquals(td.getSemaName(), "转换成小写");

        for (Map<String, Object> param : td.getParams()) {
            assertTrue(param.containsKey("name"));
        }

        td = TransformerDef.fromTransformer(TruncateTF.class);
        assertEquals(td.getTftype(), "TruncateTF");
        assertEquals(td.getSemaName(), "截取n位字符");

        for (Map<String, Object> param : td.getParams()) {
            assertTrue(param.containsKey("name"));
        }

        td = TransformerDef.fromTransformer(AddFieldTF.class);
        assertEquals(td.getTftype(), "AddFieldTF");
        assertEquals(td.getSemaName(), "新建列");

        for (Map<String, Object> param : td.getParams()) {
            assertTrue(param.containsKey("name"));
            if ("cascade".equals(param.get("type"))) {
                assertNotNull(param.get("domain"));
            }
        }
    }
}
