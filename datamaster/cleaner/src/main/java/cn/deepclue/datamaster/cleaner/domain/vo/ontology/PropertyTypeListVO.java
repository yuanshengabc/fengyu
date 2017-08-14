package cn.deepclue.datamaster.cleaner.domain.vo.ontology;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magneto on 17-4-9.
 */
public class PropertyTypeListVO {
    private List<PropertyTypeVO> propertyTypes = new ArrayList<>();
    private Integer ptCout;

    public List<PropertyTypeVO> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(List<PropertyTypeVO> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public Integer getPtCout() {
        return ptCout;
    }

    public void setPtCout(Integer ptCout) {
        this.ptCout = ptCout;
    }
}
