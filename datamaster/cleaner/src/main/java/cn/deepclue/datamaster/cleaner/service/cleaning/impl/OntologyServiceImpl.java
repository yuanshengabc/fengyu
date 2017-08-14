package cn.deepclue.datamaster.cleaner.service.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.OntologyDao;
import cn.deepclue.datamaster.cleaner.domain.vo.ontology.*;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.cleaner.service.cleaning.OntologyService;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by magneto on 17-3-31.
 */
@Service("ontologyService")
public class OntologyServiceImpl implements OntologyService {
    private static Logger logger = LoggerFactory.getLogger(OntologyServiceImpl.class);

    private static List<String> propertyGroupFilter = Arrays.asList("全部", "未分组");

    @Autowired
    private OntologyDao ontologyDao;

    @Override
    public ObjectTypeListVO getObjectTypes(int page, int pageSize) {
        List<ObjectType> ots = ontologyDao.getObjectTypes(page, pageSize);
        List<ObjectTypeVO> otVos = null;
        if (ots != null) {
            otVos = new ArrayList<>();
            for (ObjectType ot : ots) {
                ObjectTypeVO otVo = new ObjectTypeVO();
                BeanUtils.copyProperties(ot, otVo);
                otVos.add(otVo);
            }
        }

        ObjectTypeListVO otVoC = new ObjectTypeListVO();
        otVoC.setObjectTypes(otVos);
        Integer otCount = ontologyDao.getObjectTypeCount();
        otVoC.setOtCount(otCount);

        return otVoC;
    }

    @Override
    public ObjectTypeVO createObjectType(ObjectType objectType) {
        ObjectType ot = ontologyDao.createObjectType(objectType);
        ObjectTypeVO otVo = null;
        if (ot != null) {
            otVo = new ObjectTypeVO();
            BeanUtils.copyProperties(ot, otVo);
        }
        return otVo;
    }

    @Override
    public Boolean updateObjectType(ObjectType objectType) {
        return ontologyDao.updateObjectType(objectType);
    }

    @Override
    public Boolean deleteObjectType(int otid) {
        List<PropertyType> pts = ontologyDao.getPropertyTypes(otid);
        Boolean delPt = false;
        for (PropertyType pt : pts) {
            delPt = ontologyDao.deletePropertyType(pt.getPtid());
            if (!delPt) {
                throw new JdbcException(JdbcErrorEnum.DELETE);
            }
        }
        List<PropertyGroup> pgs = ontologyDao.getPropertyGroups(otid);
        Boolean delPg = false;
        for (PropertyGroup pg : pgs) {
            delPg = ontologyDao.deletePropertyGroup(pg.getPgid());
            if (!delPg) {
                throw new JdbcException(JdbcErrorEnum.DELETE);
            }
        }

        return ontologyDao.deleteObjectType(otid);
    }

    @Override
    public ObjectTypeVO getObjectType(int otid) {
        ObjectType ot = ontologyDao.getObjectType(otid);
        ObjectTypeVO otVo = null;
        if (ot != null) {
            otVo = new ObjectTypeVO();
            BeanUtils.copyProperties(ot, otVo);
        }
        return otVo;
    }

    @Override
    public PropertyTypeListVO getPropertyTypes(int otid, Integer pgid, int page, int pageSize) {
        //属性组不为空时，直接返回
        PropertyTypeListVO ptVoCt = new PropertyTypeListVO();
        List<PropertyTypeVO> ptVos = new ArrayList<>();

        Integer ptCount;
        List<PropertyType> pts;
        if (pgid != null && pgid != -1) {
            ptCount = ontologyDao.getPropertyTypeCountOfGroup(pgid);
            pts = ontologyDao.getPropertyTypes(otid, pgid, page, pageSize, "ptid");
        } else if (pgid != null && pgid == -1) {
            pts = ontologyDao.getPropertyTypesOfUngrouped(otid, page, pageSize, "ptid");
            ptCount = ontologyDao.getPropertyTypeCountOfUnGroup(otid);
        } else {
            pts = ontologyDao.getPropertyTypes(otid, page, pageSize, "ptid");
            ptCount = ontologyDao.getPropertyTypeCount(otid);
        }
        ptVoCt.setPtCout(ptCount);


        if (pts != null) {
            for (PropertyType pt : pts) {
                PropertyGroup pg = null;
                if (pt.getPgid() != null) {
                    pg = ontologyDao.getPropertyGroup(pt.getPgid());
                }
                PropertyTypeVO ptVo = new PropertyTypeVO();
                BeanUtils.copyProperties(pt, ptVo);
                ptVo.setPropertyGroup(pg);

                ptVos.add(ptVo);

            }
        }

        ptVoCt.setPropertyTypes(ptVos);

        return ptVoCt;
    }

    @Override
    public PropertyTypeVO createPropertyType(PropertyType propertyType) {
        PropertyType pt = ontologyDao.createPropertyType(propertyType);
        if (pt == null) {
            return null;
        }

        PropertyGroup pg = null;
        if (pt.getPgid() != null) {
            pg = ontologyDao.getPropertyGroup(pt.getPgid());
        }

        return PropertyTypeVO.from(pt, pg);
    }

    @Override
    public Boolean updatePropertyType(PropertyType propertyType) {
        return ontologyDao.updatePropertyType(propertyType);
    }


    @Override
    public PropertyTypeVO getPropertyType(int ptid) {
        PropertyType pt = ontologyDao.getPropertyType(ptid);
        if (pt == null) {
            return null;
        }

        PropertyGroup pg = null;
        if (pt.getPgid() != null) {
            pg = ontologyDao.getPropertyGroup(pt.getPgid());
        }

        return PropertyTypeVO.from(pt, pg);
    }

    @Override
    public Boolean deletePropertyType(int ptid) {
        return ontologyDao.deletePropertyType(ptid);
    }

    @Override
    public List<PropertyGroupVO> getPropertyGroups(int otid) {
        List<PropertyGroup> pgs = ontologyDao.getPropertyGroups(otid);

        List<PropertyGroupVO> pgVos = new ArrayList<>();
        for (PropertyGroup pg : pgs) {
            PropertyGroupVO pgVo = new PropertyGroupVO();
            BeanUtils.copyProperties(pg, pgVo);
            Integer ptCount = ontologyDao.getPropertyTypeCountOfGroup(pg.getPgid());
            pgVo.setPtCount(ptCount);
            pgVos.add(pgVo);
        }

        return pgVos;
    }

    @Override
    public PropertyGroupVO createPropertyGroup(PropertyGroup propertyGroup) {
        if (propertyGroupFilter.contains(propertyGroup.getName())) {
            throw new CleanerException(BizErrorEnum.INVALID_PROPERTYGROUP);
        }

        PropertyGroup pg = ontologyDao.createPropertyGroup(propertyGroup.getOtid(), propertyGroup.getName());
        PropertyGroupVO pgVo = new PropertyGroupVO();
        BeanUtils.copyProperties(pg, pgVo);
        Integer ptCount = ontologyDao.getPropertyTypeCountOfGroup(pg.getPgid());
        pgVo.setPtCount(ptCount);

        return pgVo;
    }


    @Override
    public Boolean updatePropertyGroup(PropertyGroup propertyGroup) {
        return ontologyDao.updatePropertyGroup(propertyGroup);
    }

    @Override
    public Boolean deletePropertyGroup(int pgid) {
        return ontologyDao.deletePropertyGroup(pgid);
    }

    @Override
    public PropertyStatsRespVO getPropertyCount(int otid) {
        PropertyStatsRespVO propertyStatsRespVO = new PropertyStatsRespVO();
        List<PropertyType> pts = ontologyDao.getPropertyTypes(otid);
        propertyStatsRespVO.setPtcount(pts.size());
        Set<Integer> pgids = new HashSet<>();
        for (PropertyType pt : pts) {
            if (pt.getPgid() != null) {
                pgids.add(pt.getPgid());
            }
        }

        propertyStatsRespVO.setPgcount(pgids.size());

        return propertyStatsRespVO;
    }
}
