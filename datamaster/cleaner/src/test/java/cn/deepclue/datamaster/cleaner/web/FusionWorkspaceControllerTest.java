package cn.deepclue.datamaster.cleaner.web;

import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.DataSourcesResqVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.ExternalDataSourceResqVO;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionWorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FusionWorkspaceControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FusionWorkspaceService fusionWorkspaceService;

    @Test
    public void addDataSourcesTest() throws Exception {
        int fwsid = 3;
        List<ExternalDataSourceResqVO> externalDataSourceResqVOs = new LinkedList<>();
        ExternalDataSourceResqVO externalDataSourceResqVO = new ExternalDataSourceResqVO();
        externalDataSourceResqVO.setDhid(1);
        externalDataSourceResqVO.setDbname("db1");
        externalDataSourceResqVO.setDtname("dt1");
        externalDataSourceResqVOs.add(externalDataSourceResqVO);
        List<Integer> datamasterSources = new LinkedList<>();
        datamasterSources.add(1);
        datamasterSources.add(2);
        datamasterSources.add(3);
        DataSourcesResqVO dataSourcesResqVO = new DataSourcesResqVO();
        dataSourcesResqVO.setExternalDataSources(externalDataSourceResqVOs);
        dataSourcesResqVO.setDatamasterSources(datamasterSources);
        given(fusionWorkspaceService.addDataSources(fwsid, dataSourcesResqVO)).willReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        java.lang.String requestJson = ow.writeValueAsString(dataSourcesResqVO);
        this.mvc.perform(post("/fusionWorkspace/" + fwsid + "/dataSources")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk());
    }

    @Test
    public void updateEntropyFieldsTest() throws Exception {
        int fwsid = 3;
        String addressCodeType = AddressCodeType.MULTI_ADDRESS_CODE.name();
        List<Integer> ptids = new ArrayList<>();
        ptids.add(1);
        ptids.add(2);
        Double threshold = 0.5;
        Integer uniquePtid = 1;

        given(fusionWorkspaceService.updateEntropyFields(fwsid, addressCodeType, ptids, threshold, uniquePtid)).willReturn(true);
        this.mvc.perform(post("/fusionWorkspace/" + fwsid + "/entropyFields")
                .param("addressCodeType", addressCodeType)
                .param("ptids", StringUtils.join(ptids.toArray(), ','))
                .param("threshold", threshold == null ? "" : threshold.toString())
                .param("uniquePtid", uniquePtid == null ? "" : uniquePtid.toString())
        ).andExpect(status().isOk());
    }
}
