package cn.deepclue.datamaster.cleaner.domain.vo.ontology;

import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.BaseType;
import org.springframework.beans.BeanUtils;

/**
 * Created by magneto on 17-4-9.
 */
public class PropertyTypeVO {
    private Integer ptid;
    private Integer otid;
    private Integer pgid;
    private String name;
    private Integer baseType;
    private String semaName;
    private String description;
    private String validationRule;
    private PropertyGroup propertyGroup;

    public static PropertyTypeVO from(PropertyType pt, PropertyGroup pg) {
        PropertyTypeVO ptVo = new PropertyTypeVO();
        if (pt != null) {
            BeanUtils.copyProperties(pt, ptVo);
        }
        if (pg != null) {
            ptVo.setPropertyGroup(pg);
        }
        return ptVo;
    }

    public PropertyGroup getPropertyGroup() {
        return propertyGroup;
    }

    public void setPropertyGroup(PropertyGroup propertyGroup) {
        this.propertyGroup = propertyGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPtid() {
        return ptid;
    }

    public void setPtid(Integer ptid) {
        this.ptid = ptid;
    }

    public Integer getOtid() {
        return otid;
    }

    public void setOtid(Integer otid) {
        this.otid = otid;
    }

    public Integer getPgid() {
        return pgid;
    }

    public void setPgid(Integer pgid) {
        this.pgid = pgid;
    }

    public Integer getBaseType() {
        return baseType;
    }

    public void setBaseType(Integer baseType) {
        this.baseType = baseType;
    }

    public BaseType getType() {
        return BaseType.getBaseType(baseType);
    }

    public String getSemaName() {
        return semaName;
    }

    public void setSemaName(String semaName) {
        this.semaName = semaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValidationRule() {
        return validationRule;
    }

    public void setValidationRule(String validationRule) {
        this.validationRule = validationRule;
    }
}
