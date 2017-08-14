package cn.deepclue.datamaster.cleaner.web.cleaning;

import cn.deepclue.datamaster.cleaner.domain.vo.ontology.*;
import cn.deepclue.datamaster.cleaner.service.cleaning.OntologyService;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by magneto on 17-4-6.
 */
@RestController
public class OntologyController {

    @Autowired
    private OntologyService ontologyService;

    /** objectType相关接口 **/
    @RequestMapping(path = "/objectTypes", method = RequestMethod.GET)
    public ObjectTypeListVO getObjectTypes(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize) {
        return ontologyService.getObjectTypes(page, pageSize);
    }

    @RequestMapping(path = "/objectTypes", method = RequestMethod.POST)
    public ObjectTypeVO createObjectType(@Valid ObjectType objectType) {
        return ontologyService.createObjectType(objectType);
    }

    @RequestMapping(path = "/objectTypes/{otid}", method = RequestMethod.GET)
    public ObjectTypeVO getObjectType(@PathVariable("otid") int otid) {
        return ontologyService.getObjectType(otid);
    }

    @RequestMapping(path = "/objectTypes/{otid}", method = RequestMethod.POST)
    public Boolean updateObjectType(@PathVariable("otid") int otid, @Valid ObjectType objectType) {
        objectType.setOtid(otid);
        return ontologyService.updateObjectType(objectType);
    }

    @RequestMapping(path = "/objectTypes/{otid}", method = RequestMethod.DELETE)
    public Boolean deleteObjectType(@PathVariable("otid") int otid) {
        return ontologyService.deleteObjectType(otid);
    }
    /** propertyType相关接口 **/
    @RequestMapping(path = "/propertyTypes", method = RequestMethod.GET)
    public PropertyTypeListVO getPropertyTypes(@RequestParam int otid, Integer pgid, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize){
        return ontologyService.getPropertyTypes(otid, pgid, page, pageSize);
    }

    @RequestMapping(path = "/propertyTypes", method = RequestMethod.POST)
    public PropertyTypeVO createPropertyType(@Valid PropertyType propertyType) {
        return ontologyService.createPropertyType(propertyType);
    }

    @RequestMapping(path = "/propertyTypes/{ptid}", method = RequestMethod.POST)
    public Boolean updatePropertyType(@PathVariable("ptid") int ptid, @Valid PropertyType propertyType) {
        propertyType.setPtid(ptid);
        return ontologyService.updatePropertyType(propertyType);
    }

    @RequestMapping(path = "/propertyTypes/{ptid}", method = RequestMethod.GET)
    public PropertyTypeVO getPropertyType(@PathVariable("ptid") int ptid) {
        return ontologyService.getPropertyType(ptid);
    }

    @RequestMapping(path = "/propertyTypes/{ptid}", method = RequestMethod.DELETE)
    public Boolean deletePropertyType(@PathVariable("ptid") int ptid) {
        return ontologyService.deletePropertyType(ptid);
    }

    /** propertyGroup相关接口 **/
    @RequestMapping(path = "/propertyGroups", method = RequestMethod.GET)
    public List<PropertyGroupVO> getPropertyGroups(@RequestParam int otid) {
        return ontologyService.getPropertyGroups(otid);
    }

    @RequestMapping(path = "/propertyGroups", method = RequestMethod.POST)
    public PropertyGroupVO createPropertyGroup(@Valid PropertyGroup propertyGroup) {
        return ontologyService.createPropertyGroup(propertyGroup);
    }

    @RequestMapping(path = "/propertyGroups/{pgid}", method = RequestMethod.POST)
    public Boolean updatePropertyGroup(@PathVariable("pgid") int pgid, @Valid PropertyGroup propertyGroup) {
        propertyGroup.setPgid(pgid);
        return ontologyService.updatePropertyGroup(propertyGroup);
    }

    @RequestMapping(path = "/propertyGroups/{pgid}", method = RequestMethod.DELETE)
    public boolean deletePropertyGroup(@PathVariable("pgid") int pgid) {
        return ontologyService.deletePropertyGroup(pgid);
    }

    @RequestMapping(path = "/propertyCount", method = RequestMethod.GET)
    public PropertyStatsRespVO getPropertyCount(@RequestParam int otid) {
        return ontologyService.getPropertyCount(otid);
    }



}
