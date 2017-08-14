package cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion;

import cn.deepclue.datamaster.model.ontology.PropertyType;

/**
 * Created by sunxingwen on 17-5-20.
 */
public class EntropyFieldBO {
    //工作空间id
    private Integer wsid;
    //属性
    private PropertyType propertyType;
    //权值
    private Double entropy;
    //是否被选择
    private Boolean selected;
    //是否唯一列
    private Boolean unique;

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Double getEntropy() {
        return entropy;
    }

    public void setEntropy(Double entropy) {
        this.entropy = entropy;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }
}
