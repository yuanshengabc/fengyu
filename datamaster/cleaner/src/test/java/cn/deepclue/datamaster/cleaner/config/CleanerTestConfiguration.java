package cn.deepclue.datamaster.cleaner.config;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DataHouseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DataSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DatabaseDao;
import cn.deepclue.datamaster.cleaner.scheduler.JobFactory;
import cn.deepclue.datamaster.cleaner.service.cleaning.TaskService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created by luoyong on 17-3-28.
 */
@TestConfiguration
public class CleanerTestConfiguration {

    @Bean
    public DataHouseDao dataHouseDao() {
        return Mockito.mock(DataHouseDao.class);
    }

    @Bean
    public DatabaseDao databaseDao() {
        return Mockito.mock(DatabaseDao.class);
    }

    @Bean
    public DataSourceDao dataSourceDao() {
        return Mockito.mock(DataSourceDao.class);
    }

    @Bean
    public TaskService taskService() {
        return Mockito.mock(TaskService.class);
    }

    @Bean
    public JobFactory jobFactory() {
        return Mockito.mock(JobFactory.class);
    }
}
