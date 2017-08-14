package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.BaseType;
import org.springframework.beans.BeanUtils;

/**
 * 融合属性信息VO
 * Created by sunxingwen on 17-5-18.
 */
public class FusionPropertyTypeVO {
    //属性id
    private Integer ptid;
    //属性名称
    private String semaName;
    //字段
    private String name;
    //字段类型
    private BaseType baseType;
    //描述
    private String description;

    public Integer getPtid() {
        return ptid;
    }

    public void setPtid(Integer ptid) {
        this.ptid = ptid;
    }

    public String getSemaName() {
        return semaName;
    }

    public void setSemaName(String semaName) {
        this.semaName = semaName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FusionPropertyTypeVO from(PropertyType propertyType) {
        BeanUtils.copyProperties(propertyType, this);
        this.setBaseType(BaseType.getBaseType(propertyType.getBaseType()));
        return this;
    }
}
