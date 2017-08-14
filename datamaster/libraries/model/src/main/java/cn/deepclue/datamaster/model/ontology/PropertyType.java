package cn.deepclue.datamaster.model.ontology;

import cn.deepclue.datamaster.model.schema.BaseType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by magneto on 17-3-31.
 */
public class PropertyType {
    private Integer ptid;
    @NotNull
    private Integer otid;
    private Integer pgid;
    @NotBlank(message = "name can not be blank")
    private String name;
    @NotNull
    @Min(0)
    @Max(5)
    private Integer baseType;
    private String semaName;
    private String description;
    private String validationRule;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseType() {
        return baseType;
    }

    public void setBaseType(Integer baseType) {
        this.baseType = baseType;
    }

    public BaseType getBaseTypeString() {
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
