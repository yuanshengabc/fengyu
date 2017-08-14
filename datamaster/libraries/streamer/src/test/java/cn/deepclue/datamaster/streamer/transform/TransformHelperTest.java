package cn.deepclue.datamaster.streamer.transform;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.transform.filter.LongRangeFilter;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;
import cn.deepclue.datamaster.streamer.transform.transformer.common.AddPrefixTF;
import cn.deepclue.datamaster.streamer.transform.transformer.common.ToLowerTF;
import cn.deepclue.datamaster.streamer.transform.transformer.common.TruncateTF;
import cn.deepclue.datamaster.streamer.transform.transformer.field.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 * Created by xuzb on 07/04/2017.
 */
public class TransformHelperTest {

    @Test
    public void testBuildTransformer() {
        Transformation transformation = new Transformation();
        transformation.setTftype("ToLowerTF");
        transformation.setFilters(null);
        transformation.addArg("sourceFieldName", "f1");
        Transformer transformer = TransformHelper.buildTransformer(transformation);
        assertTrue(transformer instanceof ToLowerTF);
        ToLowerTF toLowerTF = (ToLowerTF) transformer;
        assertEquals(toLowerTF.getSourceFieldName(), "f1");

        transformation.setTftype("TruncateTF");
        transformation.setFilters(null);
        transformation.addArg("sourceFieldName",  "f2");
        transformer = TransformHelper.buildTransformer(transformation);
        assertTrue(transformer instanceof TruncateTF);
        TruncateTF truncateTF = (TruncateTF) transformer;
        assertEquals(truncateTF.getSourceFieldName(), "f2");
    }

    @Test
    public void testBuildTransformerDefs() {
        List<TransformerDef> transformerDefs = TransformHelper.buildCommonTransformerDefs();
        for (TransformerDef transformerDef : transformerDefs) {
            assertNotNull(transformerDef.getTftype());
            assertNotNull(transformerDef.getParams());
            assertNotNull(transformerDef.getSemaName());
        }
    }

    private void testCascadeTransformer(String fieldName, Transformer transformer, boolean expect) {
        assertEquals(TransformHelper.isCascadedWith(fieldName, transformer), expect);
    }

    @Test
    public void testCascadeTransformer() {
        testCascadeTransformer("f1", new AddPrefixTF("f1", "abc"), true);
        testCascadeTransformer("f1", new AddPrefixTF("f2", "abc"), false);

        testCascadeTransformer("f1", new ConcatFieldTF("f1", "f2", "f1 - f2", "-"), true);
        testCascadeTransformer("f1", new ConcatFieldTF("f3", "f2", "f3 - f2", "-"), false);

        testCascadeTransformer("f1", new ConvertTypeTF("f1", BaseType.INT), true);
        testCascadeTransformer("f1", new ConvertTypeTF("f2", BaseType.INT), false);

        testCascadeTransformer("f1", new DelFieldTF("f1"), true);
        testCascadeTransformer("f1", new DelFieldTF("f2"), false);

        testCascadeTransformer("f1", new DupFieldTF("f1", "f2"), true);
        testCascadeTransformer("f1", new DupFieldTF("f3", "f2"), false);

        testCascadeTransformer("f1", new SplitFieldTF("f1", 2, ","), true);
        testCascadeTransformer("f1", new SplitFieldTF("f2", 2, ","), false);
    }

    private void testBuildTransformerArsg(Transformer transformer, Map<String, Object> expect) {
        Map<String, Object> args = TransformHelper.buildTransformerArgs(transformer);
        for (Map.Entry<String, Object> arg : args.entrySet()) {
            assertEquals(arg.getValue(), expect.get(arg.getKey()));
        }
    }

    @Test
    public void testBuildTransformerArgs() {
        AddFieldTF addFieldTF = new AddFieldTF("f1", "f2", BaseType.TEXT, "a");
        Map<String, Object> expect = new Hashtable<>();
        expect.put("sourceFieldName", "f1");
        expect.put("targetFieldName", "f2");
        expect.put("baseType", "TEXT");
        expect.put("valueText", "a");

        testBuildTransformerArsg(addFieldTF, expect);
    }

    @Test
    public void testDeserTransformations() {
        List<Transformation> transformations = new ArrayList<>();
        Transformation transformation = new Transformation();
        transformation.setTftype("AddFieldTF");
        transformation.addArg("sourceFieldName", "f1");
        transformation.addArg("targetFieldName", "f2");

        LongRangeFilter rangeFilter = new LongRangeFilter("f3");
        rangeFilter.setMaxValue(100l);
        rangeFilter.setMinValue(10l);

        transformation.addFilter(rangeFilter);

        transformations.add(transformation);

        String transformationStr = TransformHelper.serializeTransformations(transformations);

        List<Transformation> deserTransformations = TransformHelper.deserializeTransformations(transformationStr);

        assertEquals(deserTransformations.size(), 1);
        assertTrue(deserTransformations.get(0).getFilters().get(0) instanceof LongRangeFilter);
    }
}
