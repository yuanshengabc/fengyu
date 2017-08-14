package cn.deepclue.datamaster.cleaner.domain.bo.ontology;

import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by magneto on 17-5-23.
 */
public class ObjectTypeBO {
    private Integer otid;
    private String name;
    private String description;
    private List<PropertyType> propertyTypes;

    public Integer getOtid() {
        return otid;
    }

    public void setOtid(Integer otid) {
        this.otid = otid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PropertyType> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(List<PropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public RSSchema toRsSchema() {
        RSSchema rsSchema = new RSSchema();
        List<RSField> rsFields = new LinkedList<>();
        for (PropertyType propertyType : getPropertyTypes()) {
            RSField rsField = new RSField();
            rsField.setName(propertyType.getName());
            rsField.setBaseType(BaseType.getBaseType(propertyType.getBaseType()));
            rsFields.add(rsField);
        }
        rsSchema.setFields(rsFields);
        return rsSchema;
    }
}
