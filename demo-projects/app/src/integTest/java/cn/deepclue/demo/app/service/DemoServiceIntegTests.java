package cn.deepclue.demo.app.service;

import cn.deepclue.demo.app.domain.bo.DemoBO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luoyong on 17-6-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoServiceIntegTests {

    @Autowired
    private DemoService demoService;


    @Test
    public void getById() throws Exception {
        DemoBO bo = this.demoService.getDemoById(1L);
        assertThat(bo.getColumn1()).isEqualTo("column1");
    }
}
