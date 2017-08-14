package cn.deepclue.datamaster.streamer.transform.transformer.common;

import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;

import java.util.regex.Pattern;

/**
 * Created by xuzb on 17/04/2017.
 */
public abstract class RegexFilterTF extends SingleTextFieldTF {
    private Pattern pattern;

    public RegexFilterTF(String sourceFieldName) {
        super(sourceFieldName);
    }

    protected abstract String regex();

    @Override
    protected RSSchema prepareSchemaTF(RSSchema schema) {
        pattern = Pattern.compile(regex());

        return super.prepareSchemaTF(schema);
    }

    @Override
    protected String transformTextValue(RSField field, String value) {

        String preTransformedValue = preTransformNotNullValue(value);

        if (preTransformedValue != null &&
                pattern.matcher(preTransformedValue).matches()) {
            return transformCandidateValue(preTransformedValue);
        }

        if (preserveInvalidValue()) {
            return preTransformedValue;
        }

        return null;
    }

    protected abstract String preTransformNotNullValue(String value);

    protected abstract String transformCandidateValue(String candidateValue);

    protected boolean preserveInvalidValue() {
        return true;
    }
}
