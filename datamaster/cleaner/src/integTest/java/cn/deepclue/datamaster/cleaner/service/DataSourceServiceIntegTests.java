package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataSourceService;
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
 * Created by magneto on 17-4-6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DataSourceServiceIntegTests {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private DataSourceService dataSourceService;

    @Test
    public void removeDataSource() {
        Boolean ret = dataSourceService.removeDataSource(1);
        assertThat(ret).isTrue();

        ret = dataSourceService.removeDataSource(10);
        assertThat(ret).isFalse();
    }

    @Test
    public void fetchDataSources() {
        DataTableListVO rsVo = dataSourceService.fetchDataTables(1, "test1", false, 0, 2);
        assertThat(rsVo.getDataTables().size()).isEqualTo(2);
    }

    @Test
    public void getImportedDataSources() {
        List<DataSource> dataSources = dataSourceService.getImportedDataSources(1, 0, 2);
        assertThat(dataSources.size()).isEqualTo(2);
    }
}
