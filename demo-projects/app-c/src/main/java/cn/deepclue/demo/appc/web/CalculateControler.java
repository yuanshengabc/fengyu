package cn.deepclue.demo.appc.web;

import cn.deepclue.demo.appc.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculateControler {

    @Autowired
    CalculateService calculateService;

    @RequestMapping(value = "/calculation/{id}", method = RequestMethod.GET)
    public int calculation(@PathVariable("id") int id){

        // TODO
        // 参数合法性判断

        return calculateService.calculation(id);
    }
}
