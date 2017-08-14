package cn.deepclue.datamaster.streamer.transform;

import cn.deepclue.datamaster.streamer.exception.UnknownTransformerTypeException;
import cn.deepclue.datamaster.streamer.transform.filter.FilterDeserAdapter;
import cn.deepclue.datamaster.streamer.transform.transformer.FilterTransformer;
import cn.deepclue.datamaster.streamer.transform.transformer.SingleFieldTF;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;
import cn.deepclue.datamaster.streamer.transform.transformer.common.*;
import cn.deepclue.datamaster.streamer.transform.transformer.dst.*;
import cn.deepclue.datamaster.streamer.transform.transformer.field.*;
import cn.deepclue.datamaster.streamer.util.JsonUtils;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by xuzb on 07/04/2017.
 */
public class TransformHelper {
    private static final Logger logger = LoggerFactory.getLogger(TransformHelper.class);

    private static char[] numstr = {'0','1','2','3','4','5','6','7','8','9'};

    private TransformHelper() {}

    public static List<Transformation> deserializeTransformations(String transformation) {
        Type listType = new TypeToken<ArrayList<Transformation>>(){}.getType();
        return JsonUtils.deserializeList(transformation, listType, new FilterDeserAdapter());
    }

    public static String serializeTransformations(List<Transformation> transformations) {
        Type listType = new TypeToken<List<Transformation>>() {}.getType();
        return JsonUtils.serialize(transformations, listType, new FilterDeserAdapter());
    }

    private static Class<? extends Transformer> getCommonTransformerClass(String tftype) {
        switch (tftype) {
            case "ToLowerTF":
                return ToLowerTF.class;
            case "ToUpperTF":
                return ToUpperTF.class;
            case "TruncateTF":
                return TruncateTF.class;
            case "AddPrefixTF":
                return AddPrefixTF.class;
            case "AddSuffixTF":
                return AddSuffixTF.class;
            case "TrimTF":
                return TrimTF.class;
            case "RetainDigitTF":
                return RetainDigitTF.class;
            case "RetainAlphabetTF":
                return RetainAlphabetTF.class;
            case "ReplaceFieldTF":
                return ReplaceFieldTF.class;
            case "ReplaceTF":
                return ReplaceTF.class;
            case "AssignNullTF":
                return AssignNullTF.class;
        }

        return null;
    }

    private static Class<? extends Transformer> getFieldTransformerClass(String tftype) {
        switch (tftype) {
            case "AddFieldTF":
                return AddFieldTF.class;
            case "ConcatFieldTF":
                return ConcatFieldTF.class;
            case "ConvertTypeTF":
                return ConvertTypeTF.class;
            case "DelFieldTF":
                return DelFieldTF.class;
            case "DupFieldTF":
                return DupFieldTF.class;
            case "SplitFieldTF":
                return SplitFieldTF.class;
        }

        return null;
    }

    private static Class<? extends Transformer> getDSTTransformerClass(String tftype) {
        switch (tftype) {
            case "HKTelTF":
                return HKTelTF.class;
            case "UnifyGenderTF":
                return UnifyGenderTF.class;
            case "UnifyMarrigeTF":
                return UnifyMarrigeTF.class;
            case "IpTF":
                return IpTF.class;
            case "EmailTF":
                return EmailTF.class;
            case "HKIdTF":
                return HKIdTF.class;
            case "TWIdTF":
                return TWIdTF.class;
            case "TWTelTF":
                return TWTelTF.class;
            case "TWZipCodeTF":
                return TWZipCodeTF.class;
            // TODO: Add more dst transformer.
        }

        return null;
    }

    private static Class<? extends Transformer> getTransformerClass(String tftype) {
        Class<? extends Transformer> transformerClass = getCommonTransformerClass(tftype);
        if (transformerClass != null) {
            return transformerClass;
        }

        transformerClass = getFieldTransformerClass(tftype);
        if (transformerClass != null) {
            return transformerClass;
        }

        transformerClass = getDSTTransformerClass(tftype);
        if (transformerClass != null) {
            return transformerClass;
        }

        throw new UnknownTransformerTypeException("Unknown transformer type.", "未知转换类型。");
    }

    public static Transformer buildTransformer(Transformation transformation) {
        Transformer transformer =
                JsonUtils.fromJson(JsonUtils.toJson(transformation.getArgs()),
                        getTransformerClass(transformation.getTftype()));

        if (transformer instanceof FilterTransformer) {
            ((FilterTransformer) transformer).setFilters(transformation.getFilters());
        }

        return transformer;
    }

    public static List<TransformerDef> buildCommonTransformerDefs() {
        List<Class<? extends Transformer>> classes = new ArrayList<>();
        classes.add(AddPrefixTF.class);
        classes.add(AddSuffixTF.class);
        classes.add(RetainAlphabetTF.class);
        classes.add(RetainDigitTF.class);
        classes.add(ToLowerTF.class);
        classes.add(ToUpperTF.class);
        classes.add(TrimTF.class);
        classes.add(TruncateTF.class);
        classes.add(ReplaceFieldTF.class);
        classes.add(ReplaceTF.class);
        classes.add(AssignNullTF.class);

        return buildTransformerDefs(classes);
    }

    public static List<TransformerDef> buildDsrTransformerDefs() {
        List<Class<? extends Transformer>> classes = new ArrayList<>();
        classes.add(HKTelTF.class);
        classes.add(IpTF.class);
        classes.add(UnifyGenderTF.class);
        classes.add(UnifyMarrigeTF.class);
        classes.add(EmailTF.class);
        classes.add(HKIdTF.class);
        classes.add(TWIdTF.class);
        classes.add(TWTelTF.class);
        classes.add(TWZipCodeTF.class);

        return buildTransformerDefs(classes);
    }

    public static List<TransformerDef> buildFieldTransformerDefs() {
        List<Class<? extends Transformer>> classes = new ArrayList<>();
        classes.add(AddFieldTF.class);
        classes.add(ConcatFieldTF.class);
        classes.add(ConvertTypeTF.class);
        classes.add(DelFieldTF.class);
        classes.add(DupFieldTF.class);
        classes.add(SplitFieldTF.class);

        return buildTransformerDefs(classes);
    }

    public static TransformerDef buildTransformerDef(String tftype) {
        return TransformerDef.fromTransformer(getTransformerClass(tftype));
    }

    private static List<TransformerDef> buildTransformerDefs(List<Class<? extends Transformer>> classes) {
        List<TransformerDef> transformerDefs = new ArrayList<>();
        for (Class<? extends Transformer> clazz : classes) {
            transformerDefs.add(TransformerDef.fromTransformer(clazz));
        }

        return transformerDefs;
    }

    public static List<Transformer> buildTransformers(List<Transformation> transformations) {
        if (transformations == null) {
            return new ArrayList<>();
        }

        List<Transformer> transformers = new ArrayList<>(transformations.size());

        for (Transformation transformation : transformations) {
            transformers.add(buildTransformer(transformation));
        }

        return transformers;
    }

    public static Rewriter deserializeRewriters(String rewriterStr) {
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> fieldMappings = JsonUtils.fromJson(rewriterStr, mapType);
        return new Rewriter(fieldMappings);
    }

    public static String serializeRewriters(Rewriter rewriter) {
        return JsonUtils.toJson(rewriter.getFieldMappings());
    }

    private static char FullWidthWhiteSpace = '　'; // 全角空格
    private static char WhiteSpace = ' ';

    public static String trim(String value) {
        int len = value.length();
        int st = 0;

        while ((st < len) && (value.charAt(st) <= WhiteSpace || value.charAt(st) == FullWidthWhiteSpace)) {
            st++;
        }
        while ((st < len) && (value.charAt(len - 1) <= WhiteSpace || value.charAt(len - 1) == FullWidthWhiteSpace)) {
            len--;
        }
        return ((st > 0) || (len < value.length())) ? value.substring(st, len) : value;
    }

    public static String trimAll(String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c <= WhiteSpace || c == FullWidthWhiteSpace) {
                continue;
            }

            sb.append(c);
        }

        return sb.toString();
    }



    public static boolean isFakeSeq(String value,int unqiCSize) {
        boolean asc = true;
        boolean dsc = true;
        //判断递增，如123456
        for (int i = 1; i < value.length(); ++i) {
            char lastChar = value.charAt(i - 1);
            if ((value.charAt(i) - lastChar) != 1) {
                asc = false;
                break;
            }
        }
        //判断递减，如987654
        for (int i = 1; i < value.length(); ++i) {
            char lastChar = value.charAt(i - 1);
            if ((lastChar - value.charAt(i)) != 1) {
                dsc = false;
                break;
            }
        }

    //取某字符串中包含的不同字符数
        Set<Character> uniqChars = new HashSet<>();
        for (int i = 0; i < value.length(); ++i) {
            uniqChars.add(value.charAt(i));
        }
        return (asc || dsc || uniqChars.size() <= unqiCSize);
    }

    public static boolean isCascadedWith(String fieldName, Transformer followingTransformer) {
        if (followingTransformer instanceof SingleFieldTF) {
            String cascadeFieldName = ((SingleFieldTF) followingTransformer).getSourceFieldName();
            return fieldName.equals(cascadeFieldName);
        } else if (followingTransformer instanceof ConcatFieldTF) {
            String leftFieldName = ((ConcatFieldTF) followingTransformer).getLeftFieldName();
            if (fieldName.equals(leftFieldName)) {
                return true;
            }

            String rightFieldName = ((ConcatFieldTF) followingTransformer).getRightFieldName();
            return fieldName.equals(rightFieldName);
        } else if (followingTransformer instanceof ConvertTypeTF) {
            String cascadeFieldName = ((ConvertTypeTF) followingTransformer).getSourceFieldName();
            return fieldName.equals(cascadeFieldName);
        } else if (followingTransformer instanceof DelFieldTF) {
            String cascadeFieldName = ((DelFieldTF) followingTransformer).getSourceFieldName();
            return fieldName.equals(cascadeFieldName);
        } else if (followingTransformer instanceof DupFieldTF) {
            String cascadeFieldName = ((DupFieldTF) followingTransformer).getSourceFieldName();
            return fieldName.equals(cascadeFieldName);
        } else if (followingTransformer instanceof SplitFieldTF) {
            String cascadeFieldName = ((SplitFieldTF) followingTransformer).getSourceFieldName();
            return fieldName.equals(cascadeFieldName);
        }

        return false;
    }


    public static Map<String, Object> buildTransformerArgs(Transformer transformer) {
        List<Field> fields = getAllFields(transformer.getClass());
        Map<String, Object> args = new HashMap<>();
        for (Field field : fields) {
            SemaDef annotation = field.getAnnotation(SemaDef.class);
            if (annotation == null) {
                continue;
            }

            field.setAccessible(true);


            try {
                Object value = field.get(transformer);
                if (value != null) {
                    args.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                logger.info("{}", e);
            }
        }

        return args;
    }
    public static List<Field> getAllFields(Class<?> tagClass) {
        List<Field> allFields = new ArrayList<>();
        for (Class<?> clazz = tagClass; clazz != null && clazz != Object.class;) {
            Field classFields[] = clazz.getDeclaredFields();
            for (Field f : classFields) {
                int m = f.getModifiers();
                if (!Modifier.isStatic(m)) {
                    allFields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }

        return allFields;
    }

    //截取字符串前n位数字
    public static String getNum(String value, int n){
        int m = n;
        StringBuilder numChar = new StringBuilder();
        for (int i = 0; i < value.length(); ++i) {
            if (contain(numstr,value.charAt(i))) {
                numChar.append(value.charAt(i));
                m--;
            }
            else if (m == 0) {
                break;
            }
        }
        return numChar.toString();
    }

    //判断字符数组是否包含某字符
    public static boolean contain(char[] chars, char c) {
        for (char aChar : chars) {
            if (c == aChar) {
                return true;
            }
        }
        return false;
    }
}
