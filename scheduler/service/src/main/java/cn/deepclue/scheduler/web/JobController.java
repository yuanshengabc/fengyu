package cn.deepclue.scheduler.web;

import cn.deepclue.scheduler.domain.Task;
import cn.deepclue.scheduler.job.Job;
import cn.deepclue.scheduler.job.JobFactory;
import cn.deepclue.scheduler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private JobFactory jobFactory;

    @RequestMapping(path = "/schedule", method = RequestMethod.POST)
    public boolean schedule(@RequestBody Task task) {
        Job job = jobFactory.createJob(task);
        return jobService.schedule(job);
    }

    @RequestMapping(path = "/unschedule", method = RequestMethod.POST)
    public boolean unschedule(@RequestBody Task task) {
        Job job = jobFactory.createJob(task);
        return jobService.unschedule(job);
    }

    @RequestMapping(path = "/clear", method = RequestMethod.POST)
    public boolean clear() {
        return jobService.clear();
    }

    @RequestMapping(path = "/resume", method = RequestMethod.POST)
    public boolean resume(@RequestBody Task task) {
        Job job = jobFactory.createJob(task);
        return jobService.resume(job);
    }

    @RequestMapping(path = "/resumeAll", method = RequestMethod.POST)
    public boolean resumeAll() {
        return jobService.resumeAll();
    }
}
