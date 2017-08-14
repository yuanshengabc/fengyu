package cn.deepclue.datamaster.cleaner.domain.vo.ontology;

import java.util.List;

/**
 * Created by magneto on 17-4-12.
 */
public class ObjectTypeListVO {
    private List<ObjectTypeVO> objectTypes;
    private Integer otCount;

    public List<ObjectTypeVO> getObjectTypes() {
        return objectTypes;
    }

    public void setObjectTypes(List<ObjectTypeVO> objectTypes) {
        this.objectTypes = objectTypes;
    }

    public Integer getOtCount() {
        return otCount;
    }

    public void setOtCount(Integer otCount) {
        this.otCount = otCount;
    }
}
