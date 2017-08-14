package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.dao.cleaning.OntologyDao;
import cn.deepclue.datamaster.cleaner.domain.vo.ontology.PropertyTypeListVO;
import cn.deepclue.datamaster.cleaner.service.cleaning.OntologyService;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by magneto on 17-3-31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OntologyServiceTest {

    @MockBean
    private OntologyDao ontologyDao;

    @Autowired
    private OntologyService ontologyService;

    @Test public void getObjectTypes() throws Exception {
        List<ObjectType> objectTypes = new ArrayList<>();
        ObjectType ot1 = new ObjectType();
        ot1.setName("abc1");
        ObjectType ot2 = new ObjectType();
        ot2.setName("abc2");
        objectTypes.add(ot1);
        objectTypes.add(ot2);

        given(ontologyDao.getObjectTypes(0, 2)).willReturn(objectTypes);

        List<ObjectType> retOts = ontologyDao.getObjectTypes(0, 2);
        assertThat(retOts.size()).isEqualTo(2);
    }

    @Test public void createObjectType() throws Exception {
        ObjectType objectType = new ObjectType();
        objectType.setName("test");
        given(ontologyDao.createObjectType(objectType)).willReturn(objectType);
        assertThat(ontologyDao.createObjectType(objectType).getName()).isEqualToIgnoringCase("test");
    }

    @Test public void getObjectType() throws Exception {
        ObjectType objectType = new ObjectType();
        objectType.setOtid(1);
        given(ontologyDao.getObjectType(1)).willReturn(objectType);
        ObjectType retOt = ontologyDao.getObjectType(1);
        assertThat(retOt.getOtid()).isEqualTo(1);
    }

    @Test public void getPropertyTypes() throws Exception {
        int otid = 1;
        List<PropertyType> propertyTypes = new ArrayList<>();
        PropertyType pt1 = new PropertyType();
        pt1.setName("test1");
        pt1.setPtid(1);
        pt1.setPgid(1);
        PropertyType pt2 = new PropertyType();
        pt2.setName("test2");
        pt2.setPtid(2);
        pt2.setPgid(2);
        PropertyType pt3 = new PropertyType();
        pt3.setName("test3");
        pt3.setPtid(3);
        pt3.setPgid(1);
        PropertyType pt4 = new PropertyType();
        pt4.setName("test4");
        pt4.setPtid(4);
        PropertyGroup pg1 = new PropertyGroup();
        pg1.setPgid(1);
        pg1.setName("pg1");
        PropertyGroup pg2 = new PropertyGroup();
        pg2.setPgid(2);
        pg2.setName("pg2");


        propertyTypes.add(pt1);
        propertyTypes.add(pt3);
        propertyTypes.add(pt2);
        propertyTypes.add(pt4);

        List<PropertyType> ptOfpg1 = new ArrayList<>();
        ptOfpg1.add(pt1);
        ptOfpg1.add(pt3);

        given(ontologyDao.getPropertyTypes(otid, 1, 0, 10, "ptid")).willReturn(ptOfpg1);
        given(ontologyDao.getPropertyTypes(otid, 0, 10, "ptid")).willReturn(propertyTypes);
        given(ontologyDao.getPropertyGroup(1)).willReturn(pg1);
        given(ontologyDao.getPropertyGroup(2)).willReturn(pg2);
        PropertyTypeListVO rtPgVos1 = ontologyService.getPropertyTypes(otid, 1, 0, 10);
        assertThat(rtPgVos1.getPropertyTypes().size()).isEqualTo(2);
        rtPgVos1 = ontologyService.getPropertyTypes(otid, null, 0, 10);
        assertThat(rtPgVos1.getPropertyTypes().size()).isEqualTo(4);
    }

}
