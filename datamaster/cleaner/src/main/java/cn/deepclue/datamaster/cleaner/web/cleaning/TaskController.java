package cn.deepclue.datamaster.cleaner.web.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.service.cleaning.TaskService;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuzb on 16/03/2017.
 */
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(path = "/exportation")
     public Task exportRecordSource(@RequestParam int wsid,
                                    @RequestParam int wsversion,
                                    @RequestParam int dhid,
                                    @RequestParam String dbname,
                                    @RequestParam String dtname,
                                    @RequestParam String rewriter) {

        // Check for valid rewriter format.
        TransformHelper.deserializeRewriters(rewriter);

        return taskService.exportSource(wsid, wsversion, dhid, dbname, dtname, rewriter);
    }
}
