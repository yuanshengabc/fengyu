package com.yuansheng.web;

import com.yuansheng.service.SpringControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringControl {
    @Autowired
    private SpringControlService springControlService;

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public String getName(@PathVariable("id") Integer id) {
        return springControlService.getName(id);
    }
}
