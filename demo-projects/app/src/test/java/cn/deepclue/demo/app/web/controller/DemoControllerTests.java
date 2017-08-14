package cn.deepclue.demo.app.web.controller;


import cn.deepclue.demo.app.domain.bo.DemoBO;
import cn.deepclue.demo.app.domain.vo.DemoVO;
import cn.deepclue.demo.app.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by luoyong on 17-6-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DemoService demoService;

    @Test
    public void getById() throws Exception {
        String column1 = "column1";
        Double column2 = 3.4;
        Double column3 = 3.2;

        DemoBO bo = new DemoBO();
        bo.setColumn1(column1);
        bo.setColumn2(column2);
        bo.setColumn3(column3);

        DemoVO vo = new DemoVO();
        vo.setColumn1(column1);
        vo.setColumn2(column2);
        vo.setColumn3(column3);
        given(demoService.getDemoById(1L)).willReturn(bo);
        this.mvc.perform(get("/demo/1"))
//                .andExpect(content().string(JsonUtils.toJson(vo)))
                .andExpect(status().isOk());
    }


}
