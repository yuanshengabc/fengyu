package cn.deepclue.demo.app.service;

import cn.deepclue.demo.app.dao.DemoDao;
import cn.deepclue.demo.app.domain.bo.DemoBO;
import cn.deepclue.demo.app.domain.po.DemoPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by luoyong on 17-6-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoServiceTests {

    @MockBean
    private DemoDao demoDao;

    @Autowired
    private DemoService demoService;

    @Test
    public void getDemoById(){
        DemoPO po = new DemoPO();
        po.setId(1L);
        po.setColumn1("column1");
        po.setColumn2(2.3d);
        given(demoDao.getById(1L)).willReturn(po);

        DemoBO bo = demoService.getDemoById(1L);
        assertThat(bo.getColumn1()).isEqualTo(po.getColumn1());

    }

}
