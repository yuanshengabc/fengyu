package cn.deepclue.datamaster.fusion.stdin;

import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.FusionTestConfigurationProperties;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by xuzb on 19/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StdinTests {

    @Autowired
    private FusionTestConfigurationProperties properties;

    private List<FusionConfig> fusionSources = new ArrayList<>();
    private List<MySQLTableConfig> mysqlSources = new ArrayList<>();
    private FusionConfig fusionConfig;
    private List<PropertyType> propertyTypes = new ArrayList<>();

    @Before
    public void setUp() {
        MySQLTableConfig mySQLTableConfig = new MySQLTableConfig();
        mySQLTableConfig.setTableName("table1");
        mySQLTableConfig.setMysqlConfig(properties.getMysqlConfig());
        mysqlSources.add(mySQLTableConfig);

        mySQLTableConfig = new MySQLTableConfig();
        mySQLTableConfig.setTableName("table2");
        mySQLTableConfig.setMysqlConfig(properties.getMysqlConfig());
        mysqlSources.add(mySQLTableConfig);


        fusionConfig = new FusionConfig();
        fusionConfig.setFusionKey("1");
        fusionConfig.setHdfsConfig(properties.getHdfsConfig());


        FusionConfig fusionSource = new FusionConfig();
        fusionSource.setFusionKey("11");
        fusionSource.setHdfsConfig(properties.getHdfsConfig());

        fusionSources.add(fusionSource);

        fusionSource = new FusionConfig();
        fusionSource.setFusionKey("12");
        fusionSource.setHdfsConfig(properties.getHdfsConfig());

        fusionSources.add(fusionSource);

        PropertyType propertyType = new PropertyType();
        propertyType.setName("pt1");
        propertyType.setBaseType(BaseType.TEXT.getValue());

        propertyTypes.add(propertyType);

        propertyType = new PropertyType();
        propertyType.setName("pt2");
        propertyType.setBaseType(BaseType.LONG.getValue());

        propertyTypes.add(propertyType);
    }

    @Test
    public void testEntropyComputeInput() {
        EntropyComputeInput input = EntropyComputeInput.from(fusionSources, mysqlSources, fusionConfig, propertyTypes);
        assertThat(input.getAddress()).isEqualTo(fusionConfig.getHdfsConfig().getServerString());
        assertThat(input.getFsid()).isEqualTo(fusionConfig.getFusionKey());

        assertThat(input.getFusionKeys().size()).isEqualTo(fusionSources.size());

        for (int i = 0; i < input.getFusionKeys().size(); ++i) {
            assertThat(fusionSources.get(i).getFusionKey()).isEqualTo(input.getFusionKeys().get(i));
        }

        for (int i = 0; i < input.getDbConfigs().size(); ++i) {
            assertThat(mysqlSources.get(i).getTableName()).isEqualTo(input.getDbConfigs().get(i).getTableName());
        }
    }
}
