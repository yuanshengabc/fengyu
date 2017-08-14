package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataHouseService;
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
 * Created by luoyong on 17-3-27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DataHouseServiceIntegTests {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private DataHouseService dataHouseService;

    @Test
    public void isConnect() {
        DataHouse dh = dataHouseService.getDataHouse(1);
        Boolean ret = dataHouseService.getDataHouseConnection(dh);
        assertThat(ret).isTrue();
        dh.setIp("127.0.0.1");
        ret = dataHouseService.getDataHouseConnection(dh);
        assertThat(ret).isFalse();
    }

    @Test
    public void addDataHouse() {
        DataHouse ds = new DataHouse();
        ds.setName("abc");
        DataHouse dataHouse = dataHouseService.addDataHouse(ds);
        assertThat(dataHouse.getName()).isEqualTo("abc");
    }

    @Test
    public void updateDataHouse() {
        DataHouse dataHouse = new DataHouse();
        dataHouse.setDhid(1);
        dataHouse.setName("abc");
        Boolean ret = dataHouseService.updateDataHouse(dataHouse);
        assertThat(ret).isTrue();

        dataHouse.setDhid(10);
        dataHouse.setName("abc2");
        ret = dataHouseService.updateDataHouse(dataHouse);
        assertThat(ret).isFalse();
    }

    @Test
    public void removeDataHouse() {
        assertThat(dataHouseService.getDataHouse(1)).isNotNull();
        Boolean ret = dataHouseService.removeDataHouse(1);
        assertThat(ret).isTrue();
    }

    @Test
    public void getDataHouses() {
        List<DataHouse> dataHouses = dataHouseService.getDataHouses();
        assertThat(dataHouses.size()).isEqualTo(2);
    }

    @Test
    public void fetchDatabases() {
        List<Database> databases = dataHouseService.fetchDatabases(1);
        assertThat(databases.size()).isGreaterThan(2);
    }

    @Test
    public void fetchDatabase() {
        Database database = dataHouseService.fetchDatabase(1, "test1");
        assertThat(database.getDhid()).isEqualTo(1);
        assertThat(database.getName()).isEqualToIgnoringCase("test1");
    }

    @Test
    public void getRecordSource() {
        RecordSource recordSource = dataHouseService.getRecordSource(1);
        assertThat(recordSource.getRsid()).isEqualTo(1);
        assertThat(recordSource.getName()).isEqualToIgnoringCase("rs1");
    }

    @Test
    public void getDataHouse() {
        DataHouse dataHouse = dataHouseService.getDataHouse(1);
        assertThat(dataHouse.getDhid()).isEqualTo(1);
        assertThat(dataHouse.getName()).isEqualToIgnoringCase("test1");
    }
}
