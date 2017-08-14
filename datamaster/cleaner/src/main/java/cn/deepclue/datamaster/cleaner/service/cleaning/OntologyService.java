package cn.deepclue.datamaster.cleaner.service.cleaning;

import cn.deepclue.datamaster.cleaner.domain.vo.ontology.*;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;

import java.util.List;

/**
 * Created by magneto on 17-3-31.
 */
public interface OntologyService {
    /** objectType相关接口 **/
    ObjectTypeListVO getObjectTypes(int page, int pageSize);
    ObjectTypeVO createObjectType(ObjectType objectType);
    Boolean updateObjectType(ObjectType objectType);
    Boolean deleteObjectType(int otid);
    ObjectTypeVO getObjectType(int otid);

    /** propertyType相关接口 **/
    PropertyTypeListVO getPropertyTypes(int otid, Integer pgid, int page, int pageSize);
    PropertyTypeVO createPropertyType(PropertyType propertyType);
    Boolean updatePropertyType(PropertyType propertyType);
    PropertyTypeVO getPropertyType(int ptid);
    Boolean deletePropertyType(int ptid);

    /** propertyGroup相关接口 **/
    List<PropertyGroupVO> getPropertyGroups(int otid);
    PropertyGroupVO createPropertyGroup(PropertyGroup propertyGroup);
    Boolean updatePropertyGroup(PropertyGroup propertyGroup);
    Boolean deletePropertyGroup(int pgid);

    PropertyStatsRespVO getPropertyCount(int otid);
}
