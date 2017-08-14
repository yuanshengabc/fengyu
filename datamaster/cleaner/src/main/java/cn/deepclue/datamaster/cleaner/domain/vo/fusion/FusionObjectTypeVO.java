package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.springframework.beans.BeanUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 融合业务模型VO
 * Created by magneto on 17-5-17.
 */
public class FusionObjectTypeVO {
    //业务模型id
    private Integer otid;
    //业务模型名称
    private String name;
    //属性信息
    private List<FusionPropertyTypeVO> propertyTypes;

    public FusionObjectTypeVO fromBO(ObjectTypeBO objectTypeBO) {
        BeanUtils.copyProperties(objectTypeBO, this);
        List<FusionPropertyTypeVO> fusionPropertyTypeVOs = new LinkedList<>();
        for (PropertyType propertyType : objectTypeBO.getPropertyTypes()) {
            FusionPropertyTypeVO fusionPropertyTypeVO = new FusionPropertyTypeVO().from(propertyType);
            fusionPropertyTypeVOs.add(fusionPropertyTypeVO);
        }
        this.setPropertyTypes(fusionPropertyTypeVOs);
        return this;
    }

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

    public List<FusionPropertyTypeVO> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(List<FusionPropertyTypeVO> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }
}
