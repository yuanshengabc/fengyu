package cn.deepclue.scheduler.web;

import cn.deepclue.scheduler.domain.Callback;
import cn.deepclue.scheduler.domain.ScheduleInfo;
import cn.deepclue.scheduler.domain.SimpleScheduleInfo;
import cn.deepclue.scheduler.domain.Task;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class JobController {
    Logger logger = LoggerFactory.getLogger(JobController.class);

    @RequestMapping(path = "/executor", method = RequestMethod.POST)
    public boolean executor(@RequestBody Task task) {
        return doExecutor(task);
    }

    @RequestMapping(path = "/submit", method = RequestMethod.POST)
    public boolean submit(@RequestParam int taskId, @RequestBody ScheduleInfo scheduleInfo) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        Task task = new Task();
        task.setAppId(1);
        task.setTaskId(taskId);

        Callback callback = new Callback();
        callback.setUrl("http://127.0.0.1:7779/client/executor");
        callback.setRequestBody(new Gson().toJson(task));
        task.setCallback(callback);

        task.setScheduleInfo(scheduleInfo);

        HttpEntity<String> formEntity = new HttpEntity<>(new Gson().toJson(task), headers);
        restTemplate.postForObject("http://127.0.0.1:7778/scheduler/schedule", formEntity, Boolean.class);

        return true;
    }

    private boolean doExecutor(Task task) {
        logger.info(task.getAppId() + "." + task.getTaskId() + "被执行");

        return true;
    }
}
