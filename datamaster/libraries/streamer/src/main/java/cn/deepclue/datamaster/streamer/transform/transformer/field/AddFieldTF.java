package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.io.RecordConverter;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xuzb on 14/04/2017.
 */
@TFDef(semaName = "新建列", summary = "新建列 ${targetFieldName}", order = 1)
public class AddFieldTF extends InsertFieldTransformerTF {

    private static final Logger logger = LoggerFactory.getLogger(AddFieldTF.class);

    @SemaDef(semaName = "目标列", type = "column")
    private String sourceFieldName;

    @SemaDef(semaName = "列名", type = "text")
    private String targetFieldName;

    @SemaDef(semaName = "基本类型",
            type = "enum",
            domain = "{\"TEXT\": \"文本类型\", \"INT\": \"INT类型\", \"LONG\": \"LONG类型\", " +
                    "\"FLOAT\": \"Float类型\", \"DOUBLE\": \"Double类型\", \"DATE\": \"日期类型\"}")
    private String baseType;

    @SemaDef(semaName = "填充值", type = "cascade",
            require = false, description = "不填写即为空", domain = "{\"paramName\": \"baseType\", " +
            "\"typeMappings\": {\"DOUBLE\": \"number\", " +
                                "\"FLOAT\": \"number\"," +
                                "\"INT\": \"number\"," +
                                "\"LONG\": \"number\"," +
                                "\"TEXT\": \"text\"," +
                                "\"DATE\": \"date\"}" +
            "}"
    )
    private String valueText;

    private Object value;

    public AddFieldTF(String sourceFieldName, String targetFieldName, BaseType baseType, String valueText) {
        this.sourceFieldName = sourceFieldName;
        this.targetFieldName = targetFieldName;
        this.baseType = baseType.name();
        this.valueText = valueText;
    }

    @Override
    protected int computeInsertPosition(RSSchema schema) {
        return schema.getFieldIndex(sourceFieldName);
    }

    @Override
    protected RSField createField(RSSchema schema) {
        return new RSField(targetFieldName, BaseType.valueOf(baseType));
    }

    @Override
    protected Object createValue(Record record) {
        return value;
    }

    @Override
    protected boolean checkValid(RSSchema schema) {
        if (valueText == null || "".equals(valueText)) {
            return true;
        }

        try {
            value = RecordConverter.toBaseTypeValue(BaseType.valueOf(baseType), valueText);
            return true;
        } catch (NumberFormatException e) {
            logger.info("New field value is invalid {}", e);
            return false;
        }
    }

    @Override
    protected String getErrorMsg(RSSchema schema) {
        return "invalid " + baseType + " type field value: " + valueText;
    }

    @Override
    protected String getLocalizedErrorMsg(RSSchema schema) {
        return "无效的" + baseType + "类型的字段值: " + valueText;
    }

    @Override
    public boolean isCascadedWith(Transformer followingTransformer) {
        return TransformHelper.isCascadedWith(targetFieldName, followingTransformer);
    }
}
