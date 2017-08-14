package cn.deepclue.datamaster.cleaner.web;

import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.CleaningWorkspaceVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionWorkspaceVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceVO;
import cn.deepclue.datamaster.cleaner.service.WorkspaceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by magneto on 17-5-15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkspaceControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WorkspaceService workspaceService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void setWorkspaceService() throws Exception {

    }

    @Test
    public void getWorkspace() throws Exception {
        CleaningWorkspaceVO cleaningWorkspaceVo = new CleaningWorkspaceVO();
        FusionWorkspaceVO fusionWorkspaceVo = new FusionWorkspaceVO();
        WorkspaceVO workspaceVO = new WorkspaceVO();
        given(workspaceService.getWorkspace(1)).willReturn(workspaceVO);
        given(workspaceService.getWorkspace(2)).willReturn(cleaningWorkspaceVo);
        given(workspaceService.getWorkspace(3)).willReturn(fusionWorkspaceVo);
        MvcResult result = this.mvc.perform(get("/workspaces/2")).andExpect(status().isOk())
                .andReturn();

        this.mvc.perform(get("/workspaces/1")).andExpect(status().isOk());
        this.mvc.perform(get("/workspaces/2")).andExpect(status().isOk());
        this.mvc.perform(get("/workspaces/3")).andExpect(status().isOk());
    }

    @Test
    public void updateWorkspace() throws Exception {

    }

    @Test
    public void deleteWorkspace() throws Exception {

    }

    @Test
    public void getWorkspaces() throws Exception {

    }

    @Test
    public void createWorkspace() throws Exception {

    }

}
