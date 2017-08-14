package cn.deepclue.datamaster.streamer.transform.transformer;

import cn.deepclue.datamaster.streamer.exception.MissingDefAnnotationException;
import cn.deepclue.datamaster.streamer.transform.SemaDef;
import cn.deepclue.datamaster.streamer.transform.TFDef;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.util.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by xuzb on 05/04/2017.
 */
public class TransformerDef {
    private String tftype;
    private String semaName;
    private String description;
    private boolean alert;
    private String alertMsg;
    private String summary;
    private int order;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private List<Map<String, Object>> params;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public String getAlertMsg() {
        return alertMsg;
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
    }

    public String getSemaName() {
        return semaName;
    }

    public void setSemaName(String semaName) {
        this.semaName = semaName;
    }

    public String getTftype() {
        return tftype;
    }

    public void setTftype(String tftype) {
        this.tftype = tftype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addParam(DefParam defParam) {
        if (this.params == null) {
            this.params = new ArrayList<>();
        }

        this.params.add(defParam.getParams());
    }

    /**
     * Build transformer def from a transformer class
     * @param tfClass The transformer class
     * @return Transformer Def
     */
    public static TransformerDef fromTransformer(Class<? extends Transformer> tfClass) {
        // Collect tf def info
        TFDef classTFDef = tfClass.getAnnotation(TFDef.class);
        if (classTFDef == null) {
            throw new MissingDefAnnotationException("Missing def annotation.", "丢失注解。");
        }

        TransformerDef tfd = new TransformerDef();
        String defName = classTFDef.name();
        if (defName == null || "".equals(defName)) {
            defName = tfClass.getSimpleName();
        }

        tfd.setTftype(defName);
        tfd.setSemaName(classTFDef.semaName());
        tfd.setDescription(classTFDef.description());
        tfd.setAlert(classTFDef.alert());
        tfd.setAlertMsg(classTFDef.alertMsg());
        tfd.setOrder(classTFDef.order());
        tfd.setSummary(classTFDef.summary());

        // Collect params def info
        List<Field> fields = TransformHelper.getAllFields(tfClass);

        Type mapType = new TypeToken<HashMap<Object, String>>(){}.getType();
        for (Field field : fields) {
            SemaDef fieldSemaDef = field.getAnnotation(SemaDef.class);
            if (fieldSemaDef == null) {
                continue;
            }

            String fieldName = fieldSemaDef.name();
            if (fieldName == null || "".equals(fieldName)) {
                fieldName = field.getName();
            }

            DefParam defParam = new DefParam();
            defParam.addParamPair("name", fieldName);
            defParam.addParamPair("semaName", fieldSemaDef.semaName());
            String fieldType = fieldSemaDef.type();
            defParam.addParamPair("type", fieldType);
            if ("number".equals(fieldType)) {
                defParam.addParamPair("minValue", fieldSemaDef.minValue());
                defParam.addParamPair("maxValue", fieldSemaDef.maxValue());
            }

            String domain = fieldSemaDef.domain();
            if ("enum".equals(fieldType) || "boolean".equals(fieldType)) {
                defParam.addParamPair("domain", JsonUtils.fromJson(domain, mapType));
            } else if ("cascade".equals(fieldType)) {
                defParam.addParamPair("domain", JsonUtils.fromJson(domain, CascadeDomain.class));
            } else {
                defParam.addParamPair("domain", fieldSemaDef.domain());
            }
            defParam.addParamPair("description", fieldSemaDef.description());
            defParam.addParamPair("defaultValue", fieldSemaDef.defaultValue());
            defParam.addParamPair("order", String.valueOf(fieldSemaDef.order()));
            defParam.addParamPair("require", fieldSemaDef.require());

            tfd.addParam(defParam);
        }

        return tfd;
    }

    public List<Map<String, Object>> getParams() {
        return params;
    }
}
