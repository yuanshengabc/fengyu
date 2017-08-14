package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.service.cleaning.RecordSourceService;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by xuzb on 08/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RecordSourceServiceIntegTests {
    @Autowired
    private RecordSourceService recordSourceService;

    @Test
    public void getSchema() {
        RSSchema schema = recordSourceService.getSchema(1);
        assertThat(schema.getName()).isEqualTo("rs1");
    }
}
