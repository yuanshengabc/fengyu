package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.domain.vo.ontology.*;
import cn.deepclue.datamaster.cleaner.service.cleaning.OntologyService;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by magneto on 17-4-1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OntologyServiceintegTests {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private OntologyService ontologyService;

    @Test public void getObjectTypes() {
        ObjectTypeListVO objectTypes = ontologyService.getObjectTypes(0, 10);
        assertThat(objectTypes.getObjectTypes().size()).isLessThanOrEqualTo(10);

        thrown.expect(IllegalArgumentException.class);
        ontologyService.getObjectTypes(-1, 10);
    }

    @Test public void createObjectType() {
        ObjectType objectType = new ObjectType();
        objectType.setName("ot10");
        ObjectTypeVO retOt = ontologyService.createObjectType(objectType);
        assertThat(retOt.getName()).isEqualToIgnoringCase("ot10");
    }

    @Test public void getObjectType() {
        ObjectTypeVO ot1 = ontologyService.getObjectType(1);
        assertThat(ot1.getOtid()).isEqualTo(1);
        assertThat(ot1.getName()).isEqualToIgnoringCase("ot1");

        ot1 = ontologyService.getObjectType(1000);
        assertThat(ot1).isNull();
    }

    @Test public void updateObjectType() {
        ObjectType objectType = new ObjectType();
        objectType.setOtid(1);
        objectType.setName("otTest");
        objectType.setDescription("ot update test");
        Boolean ret = ontologyService.updateObjectType(objectType);
        assertThat(ret).isTrue();
    }

    @Test public void deleteObjectType() {
        int otid = 1;
        Boolean ret = ontologyService.deleteObjectType(otid);
        assertThat(ret).isTrue();
    }

    @Test public void getPropertyTypes() {
        PropertyTypeListVO ptVoCt = ontologyService.getPropertyTypes(1, null, 0, 10);
        assertThat(ptVoCt.getPtCout()).isEqualTo(6);

        ptVoCt = ontologyService.getPropertyTypes(1, 1, 0, 10);
        assertThat(ptVoCt.getPtCout()).isEqualTo(2);
    }

    @Test public void createPropertyType() {
        PropertyType propertyType = new PropertyType();
        propertyType.setName("pt9");
        PropertyTypeVO retPt = ontologyService.createPropertyType(propertyType);
        assertThat(retPt.getName()).isEqualToIgnoringCase("pt9");
    }

    @Test public void updatePropertyType() {
        PropertyType propertyType = new PropertyType();
        propertyType.setPtid(1);
        propertyType.setName("test1");

        Boolean ret = ontologyService.updatePropertyType(propertyType);
        assertThat(ret).isTrue();
    }

    @Test public void createPropertyGroup() {
        PropertyGroup pg = new PropertyGroup();
        pg.setOtid(1);
        pg.setName("pg9");
        PropertyGroupVO retPg = ontologyService.createPropertyGroup(pg);
        assertThat(retPg.getName()).isEqualToIgnoringCase("pg9");
    }

    @Test public void getPropertyType() {
        PropertyTypeVO propertyType = ontologyService.getPropertyType(1);
        assertThat(propertyType.getPtid()).isEqualTo(1);
        assertThat(propertyType.getName()).isEqualToIgnoringCase("pt1");

        propertyType = ontologyService.getPropertyType(1000);
        assertThat(propertyType).isNull();
    }

    @Test public void getPropertyGroups() {
        List<PropertyGroupVO> pgVos = ontologyService.getPropertyGroups(1);
        assertThat(pgVos.size()).isEqualTo(2);

        pgVos  = ontologyService.getPropertyGroups(1000);
        assertThat(pgVos.size()).isEqualTo(0);
    }

    @Test public void updatePropertyGroup() {
        PropertyGroup propertyGroup = new PropertyGroup();
        propertyGroup.setPgid(1);
        propertyGroup.setName("test1");
        Boolean ret = ontologyService.updatePropertyGroup(propertyGroup);
        assertThat(ret).isTrue();
    }

    @Test public void deletePropertyType() {
        Boolean ret = ontologyService.deletePropertyType(1);
        assertThat(ret).isTrue();

        ret = ontologyService.deletePropertyType(1000);
        assertThat(ret).isFalse();
    }

    @Test public void deletePropertyGroup() {
        Boolean ret = ontologyService.deletePropertyGroup(1);
        assertThat(ret).isTrue();

        ret = ontologyService.deletePropertyGroup(1000);
        assertThat(ret).isFalse();
    }
}
