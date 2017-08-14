package cn.deepclue.demo.appa.web;

import cn.deepclue.demo.appa.service.impl.DemoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoControler {

    @Autowired
    DemoServiceImpl demoService;

    @RequestMapping(value = "/desc/{id}", method = RequestMethod.GET)
    public String desc(@PathVariable("id") int id){

        // TODO
        // 参数合法性判断

        return demoService.getDesc(id);
    }
}
