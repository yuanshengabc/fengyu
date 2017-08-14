package cn.deepclue.datamaster.cleaner.web;

import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.CreateWorkspaceReqVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceListRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceVO;
import cn.deepclue.datamaster.cleaner.service.WorkspaceService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by xuzb on 16/03/2017.
 */
@RestController
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;

    public void setWorkspaceService(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @RequestMapping("/workspaces/{wsid}")
    public WorkspaceVO getWorkspace(@PathVariable("wsid") int wsid) {
        return workspaceService.getWorkspace(wsid);
    }

    @RequestMapping(path = "/workspaces/{wsid}", method = RequestMethod.POST)
    public boolean updateWorkspace(@PathVariable("wsid") int wsid, @RequestParam String name, String description) {
        return workspaceService.updateWorkspace(wsid, name, description);
    }

    @RequestMapping(path = "/workspaces/{wsid}", method = RequestMethod.DELETE)
    public boolean deleteWorkspace(@PathVariable("wsid") int wsid) {
        return workspaceService.deleteWorkspace(wsid);
    }

    @RequestMapping("/workspaces")
    public WorkspaceListRespVO getWorkspaces(
            @RequestParam(defaultValue = "0") int page,
            @Range(min = 10, max = 50) @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "ALL") String wstype,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(required = false) String keyword) {
        return workspaceService.getWorkspaces(page, pageSize,
                "ALL".equalsIgnoreCase(wstype) ? null : WorkspaceType.valueOf(wstype),
                "ALL".equalsIgnoreCase(status) ? null : FinishedStatus.valueOf(status),
                keyword == null ? null : keyword.trim());
    }

    @RequestMapping(path = "/workspaces", method = RequestMethod.POST)
    public boolean createWorkspace(@Valid CreateWorkspaceReqVO workspace) {
        return workspaceService.createWorkspace(workspace) >= 0;
    }

}
