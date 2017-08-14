package cn.deepclue.datamaster.cleaner.web;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataHouseService;
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
 * Created by luoyong on 17-3-27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DataHouseControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DataHouseService dataHouseService;

    @Test
    public void getDataHouses() throws Exception {
        DataHouse dataHouse = new DataHouse();
        dataHouse.setName("test");
        dataHouse.setIp("127.0.0.1");
        dataHouse.setUsername("test");
        dataHouse.setPassword("123");
        given(dataHouseService.getDataHouse(1)).willReturn(dataHouse);
        this.mvc.perform(get("/dataHouses/1")) // .andExpect(content().string(JsonUtils.toJson(dataHouse)))
                .andExpect(status().isOk());
    }
}
