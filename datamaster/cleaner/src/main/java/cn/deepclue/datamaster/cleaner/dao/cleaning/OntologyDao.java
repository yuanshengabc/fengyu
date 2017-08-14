package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;

import java.util.List;

/**
 * Created by magneto on 17-3-31.
 */
public interface OntologyDao {
    /** objectType相关接口 **/
    List<ObjectType> getObjectTypes(int page, int pageSize);
    ObjectType createObjectType(ObjectType objectType);
    ObjectType getObjectType(int otid);
    Integer getObjectTypeCount();
    Boolean updateObjectType(ObjectType objectType);
    Boolean deleteObjectType(int otid);

    /** propertyType相关接口 **/
    List<PropertyType> getPropertyTypes(int otid, int page, int pageSize, String orderBy);
    List<PropertyType> getPropertyTypesOfUngrouped(int otid, int page, int pageSize, String orderBy);
    List<PropertyType> getPropertyTypes(int otid, int pgid, int page, int pageSize, String orderBy);
    List<PropertyType> getPropertyTypes(int otid);
    Integer getPropertyTypeCount(int otid);
    Integer getPropertyTypeCountOfGroup(Integer pgid);
    Integer getPropertyTypeCountOfUnGroup(int otid);
    PropertyType createPropertyType(PropertyType propertyType);
    Boolean updatePropertyType(PropertyType propertyType);
    PropertyType getPropertyType(int ptid);
    Boolean deletePropertyType(int ptid);

    /** propertyGroup相关接口 **/
    PropertyGroup createPropertyGroup(int otid, String name);
    PropertyGroup getPropertyGroup(int pgid);
    List<PropertyGroup> getPropertyGroups(int otid);
    Boolean updatePropertyGroup(PropertyGroup propertyGroup);
    Boolean deletePropertyGroup(int pgid);

}
