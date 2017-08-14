package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.OntologyDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by magneto on 17-3-31.
 */
@Repository("ontologyDao")
public class OntologyImpl implements OntologyDao {
    Logger logger = LoggerFactory.getLogger(OntologyImpl.class);

    @Autowired
    private OntologyMapper ontologyMapper;

    @Override
    public List<ObjectType> getObjectTypes(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            String errorMsg = String.format("非法参数page:%d,pageSize:%d", page, pageSize);
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        List<ObjectType> objectTypes = ontologyMapper.getObjectTypes(page * pageSize, pageSize);
        if (objectTypes == null) {
            throw new JdbcException(JdbcErrorEnum.SELECT_OBJECT_TYPES);
        }

        return objectTypes;
    }

    @Override
    public ObjectType createObjectType(ObjectType objectType) {
        objectType.setOtid(null);
        if (ontologyMapper.insertObjectType(objectType) && objectType.getOtid() != null) {
            return objectType;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_OBJECT_TYPE);
    }

    @Override
    public ObjectType getObjectType(int otid) {
        return ontologyMapper.getObjectType(otid);
    }

    @Override
    public Integer getObjectTypeCount() {
        return ontologyMapper.getObjectTypeCount();
    }

    @Override
    public Boolean updateObjectType(ObjectType objectType) {
        return ontologyMapper.updateObjectType(objectType);
    }

    @Override
    public Boolean deleteObjectType(int otid) {
        return ontologyMapper.deleteObjectType(otid);
    }

    @Override
    public List<PropertyType> getPropertyTypes(int otid, int page, int pageSize, String orderBy) {
        return ontologyMapper.getPropertyTypes(otid, page * pageSize, pageSize, orderBy);
    }

    @Override
    public List<PropertyType> getPropertyTypesOfUngrouped(int otid, int page, int pageSize, String orderBy) {
        return ontologyMapper.getPropertyTypesOfUnGrouped(otid, page * pageSize, pageSize, orderBy);
    }

    @Override
    public List<PropertyType> getPropertyTypes(int otid, int pgid, int page, int pageSize,
                                               String orderBy) {
        return ontologyMapper.getPropertyTypesOfGroupOnePage(otid, pgid, page * pageSize, pageSize, orderBy);
    }

    @Override
    public List<PropertyType> getPropertyTypes(int otid) {
        return ontologyMapper.getAllPropertyTypes(otid);
    }

    @Override
    public Integer getPropertyTypeCount(int otid) {
        return ontologyMapper.getPropertyTypeCount(otid);
    }

    @Override
    public Integer getPropertyTypeCountOfGroup(Integer pgid) {
        return ontologyMapper.getPropertyTypeCountOfGroup(pgid);
    }

    @Override
    public Integer getPropertyTypeCountOfUnGroup(int otid) {
        return ontologyMapper.getPropertyTypeCountOfUnGroup(otid);
    }

    @Override
    public PropertyType createPropertyType(PropertyType propertyType) {
        propertyType.setPtid(null);

        if (ontologyMapper.insertPropertyType(propertyType) && propertyType.getPtid() != null) {
            return propertyType;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_PROPERTY_TYPE);
    }

    @Override
    public Boolean updatePropertyType(PropertyType propertyType) {
        return ontologyMapper.updatePropertyType(propertyType);
    }


    @Override
    public PropertyType getPropertyType(int ptid) {
        return ontologyMapper.getPropertyType(ptid);
    }

    @Override
    public Boolean deletePropertyType(int ptid) {
        PropertyType propertyType = getPropertyType(ptid);
        return (propertyType != null && ontologyMapper.deletePropertyType(ptid));
    }

    @Override
    public PropertyGroup createPropertyGroup(int otid, String name) {
        PropertyGroup propertyGroup = new PropertyGroup();
        propertyGroup.setPgid(null);
        propertyGroup.setName(name);
        propertyGroup.setOtid(otid);


        if (ontologyMapper.insertPropertyGroup(propertyGroup) && propertyGroup.getPgid() != null) {
            return propertyGroup;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_PROPERTY_GROUP);
    }


    @Override
    public PropertyGroup getPropertyGroup(int pgid) {
        return ontologyMapper.getPropertyGroup(pgid);
    }

    @Override
    public List<PropertyGroup> getPropertyGroups(int otid) {
        List<PropertyGroup> pgs = ontologyMapper.getPropertyGroups(otid);

        if (pgs != null) {
            return pgs;
        }

        throw new JdbcException(String.format("Failed to get otid:%d property group list！", otid),
                String.format("获取otid:%d属性组列表失败！", otid),
                JdbcErrorEnum.SELECT);
    }

    @Override
    public Boolean updatePropertyGroup(PropertyGroup propertyGroup) {
        return ontologyMapper.updatePropertyGroup(propertyGroup);
    }

    @Override
    public Boolean deletePropertyGroup(int pgid) {
        PropertyGroup propertyGroup = getPropertyGroup(pgid);
        return (propertyGroup != null && ontologyMapper.deletePropertyGroup(pgid));
    }
}
