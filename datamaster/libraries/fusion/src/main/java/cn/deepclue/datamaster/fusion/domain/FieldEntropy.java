package cn.deepclue.datamaster.fusion.domain;

/**
 * Created by xuzb on 09/05/2017.
 */
public class FieldEntropy {
    private String fieldName;
    private double entropy;
    private Boolean uniqued;

    public FieldEntropy() {
    }

    public FieldEntropy(String fieldName, double entropy) {
        this.fieldName = fieldName;
        this.entropy = entropy;
    }

    public boolean isUniqued() {
        return uniqued == null ? false : uniqued;
    }

    public void setUniqued(boolean uniqued) {
        this.uniqued = uniqued;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }
}
