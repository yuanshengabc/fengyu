package cn.deepclue.datamaster.cleaner.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by luoyong on 17-3-27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DataHouseControllerIntegTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getDataHouses() throws Exception {
        this.mvc.perform(get("/dataHouses/1")) //.andExpect(content().string(""))
                .andExpect(status().isOk());
    }
}
