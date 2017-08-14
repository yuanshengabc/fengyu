package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.DateConvertHelper;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuzb on 14/04/2017.
 */
@TFDef(semaName = "类型转换", order = 1, alert = true,
        summary = "列 ${sourceFieldName} 转换数据类型为${targetType}",
        alertMsg = "列【${sourceFieldName}】的类型与匹配模型类型${targetType}不符。\n\n" +
                "确认与该模型匹配，将强制转换该列的类型为${targetType}！")
public class ConvertTypeTF extends FieldTransformerTF {

    @SemaDef(semaName = "源列", type = "column")
    private String sourceFieldName;

    @SemaDef(semaName = "基本类型",
            type = "enum",
            domain = "{\"TEXT\": \"文本类型\", \"INT\": \"INT类型\", \"LONG\": \"LONG类型\", " +
                    "\"FLOAT\": \"Float类型\", \"DOUBLE\": \"Double类型\", \"DATE\": \"日期类型\"}")
    private BaseType targetType;

    private BaseType sourceType;
    private int fieldIndex;

    private List<String> cacheDateFormat;

    private SimpleDateFormat simpleDateFormat;

    public ConvertTypeTF(String sourceFieldName, BaseType targetType) {
        this.sourceFieldName = sourceFieldName;
        this.targetType = targetType;
    }

    @Override
    protected void prepareSchemaCopy(RSSchema schemaCopy) {
        fieldIndex = schemaCopy.getFieldIndex(sourceFieldName);
        RSField field = schemaCopy.getField(sourceFieldName);
        sourceType = field.getBaseType();
        field.setBaseType(targetType);
        this.cacheDateFormat = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    protected void transformRecordCopy(Record recordCopy) {
        Object value = recordCopy.getValue(fieldIndex);
        if (value == null) {
            return;
        }

        if (targetType == sourceType) {
            return;
        }

        Object targetValue = null;
        switch (targetType) {
            case TEXT:
                targetValue = convertToText(value, sourceType);
                break;
            case INT:
                targetValue = convertToInt(value, sourceType);
                break;
            case LONG:
                targetValue = convertToLong(value, sourceType);
                break;
            case FLOAT:
                targetValue = convertToFloat(value, sourceType);
                break;
            case DOUBLE:
                targetValue = convertToDouble(value, sourceType);
                break;
            case DATE:
                targetValue = convertToDate(value, sourceType);
                break;
            default:
                break;
        }

        recordCopy.setValue(fieldIndex, targetValue);
    }

    private String convertToText(Object value, BaseType sourceType) {

        if (BaseType.DOUBLE == sourceType || BaseType.FLOAT == sourceType) {
            return new BigDecimal(value.toString()).stripTrailingZeros().toPlainString();
        } else if (BaseType.DATE == sourceType) {
            return simpleDateFormat.format(value);
        }

        return value.toString();
    }

    private Integer convertToInt(Object value, BaseType sourceType) {

        Integer targetInt = null;
        switch (sourceType) {
            case TEXT:
                try {
                    targetInt = Integer.parseInt(value.toString());
                } catch (NumberFormatException ignored) {
                }
                break;
            case LONG:
                Long longValue = (Long) value;
                if (longValue < Integer.MAX_VALUE && longValue > Integer.MIN_VALUE) {
                    targetInt = longValue.intValue();
                }
                break;
            case FLOAT:
                Float floatValue = (Float) value;
                if (floatValue < Integer.MAX_VALUE && floatValue > Integer.MIN_VALUE) {
                    targetInt = floatValue.intValue();
                }
                break;
            case DOUBLE:
                Double doubleValue = (Double) value;
                if (doubleValue < Integer.MAX_VALUE && doubleValue > Integer.MIN_VALUE) {
                    targetInt = doubleValue.intValue();
                }
                break;
            case INT:
            case DATE:
            default:
                break;
        }
        return targetInt;
    }

    private Long convertToLong(Object value, BaseType sourceType) {

        Long targetLong = null;
        switch (sourceType) {
            case TEXT:
                try {
                    targetLong = Long.parseLong(value.toString());
                } catch (NumberFormatException ignored) {
                }
                break;
            case INT:
            case FLOAT:
            case DOUBLE:
                targetLong = ((Number) value).longValue();
                break;
            case DATE:
                targetLong = ((Date) value).getTime();
                break;
            case LONG:
            default:
                break;
        }
        return targetLong;
    }

    private Float convertToFloat(Object value, BaseType sourceType) {

        Float targetFloat = null;
        switch (sourceType) {
            case TEXT:
                try {
                    targetFloat = Float.parseFloat(value.toString());
                } catch (NumberFormatException ignored) {
                }
                break;
            case INT:
            case LONG:
            case DOUBLE:
                targetFloat = ((Number) value).floatValue();
                break;
            case FLOAT:
            case DATE:
            default:
                break;
        }
        return targetFloat;
    }

    private Double convertToDouble(Object value, BaseType sourceType) {

        Double targetDouble = null;
        switch (sourceType) {
            case TEXT:
                try {
                    targetDouble = Double.parseDouble(value.toString());
                } catch (NumberFormatException ignored) {
                }
                break;
            case INT:
            case LONG:
            case FLOAT:
                targetDouble = ((Number) value).doubleValue();
                break;
            case DOUBLE:
            case DATE:
            default:
                break;
        }
        return targetDouble;
    }

    private Date convertToDate(Object value, BaseType sourceType) {

        Date targetDate = null;
        switch (sourceType) {
            case TEXT:
                targetDate = textToDate(value);
                break;
            case INT:
                Integer intValue = (Integer) value;
                targetDate = new Date(intValue * 1000);
                break;
            case LONG:
                targetDate = longToDate(value);
                break;
            case FLOAT:
            case DOUBLE:
            case DATE:
            default:
                break;
        }
        return targetDate;
    }

    @Override
    protected boolean checkValid(RSSchema schema) {
        if (sourceType == targetType) {
            return false;
        }

        if (sourceType == BaseType.DATE && targetType == BaseType.INT) {
            return false;
        }

        if (sourceType == BaseType.DATE && targetType == BaseType.FLOAT) {
            return false;
        }

        if (sourceType == BaseType.DATE && targetType == BaseType.DOUBLE) {
            return false;
        }

        if (sourceType == BaseType.FLOAT && targetType == BaseType.DATE) {
            return false;
        }

        if (sourceType == BaseType.DOUBLE && targetType == BaseType.DATE) {
            return false;
        }
        return true;
    }

    @Override
    protected String getErrorMsg(RSSchema schema) {
        return "Convert not support.";
    }

    @Override
    protected String getLocalizedErrorMsg(RSSchema schema) {
        return "不支持的类型转换";
    }

    private Date textToDate(Object value) {
        Date targetDate = null;
        if (!cacheDateFormat.isEmpty()) {
            for (String format : cacheDateFormat) {
                try {
                    targetDate = DateConvertHelper.parseFormat(format, value.toString());
                    break;
                } catch (ParseException ignored) {
                }
            }
        }
        if (null == targetDate) {
            targetDate = DateConvertHelper.hitParseFormat(cacheDateFormat, value.toString());
        }
        return targetDate;
    }

    private Date longToDate(Object value) {
        Long longValue = ((Number) value).longValue();
        if (longValue < DateConvertHelper.TIME_MILLIS_THRESHOLD) {
            return new Date(longValue * 1000);
        } else {
            return new Date(longValue);
        }
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }
}
