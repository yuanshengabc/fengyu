package cn.deepclue.datamaster.streamer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by xuzb on 07/04/2017.
 */
@SpringBootConfiguration
@ComponentScan(basePackages = {"cn.deepclue.datamaster.streamer"})
@EnableConfigurationProperties(StreamerIntegTestProperties.class)
public class StreamerIntegTestConfiguration {
    @Autowired
    private StreamerIntegTestProperties properties;

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(properties.getMysqlConfig().getUsername());
        dataSource.setPassword(properties.getMysqlConfig().getPassword());
        dataSource.setUrl(properties.getMysqlConfig().getConnectionUrl());
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
