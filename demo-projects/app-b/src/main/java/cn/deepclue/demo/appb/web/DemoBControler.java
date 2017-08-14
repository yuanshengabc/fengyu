package cn.deepclue.demo.appb.web;

import cn.deepclue.demo.appb.service.impl.DemoServiceImpl;
import cn.deepclue.demo.appb.service.feign.ICalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoBControler {

    @Autowired
    DemoServiceImpl dmoBService;

    @Autowired
    ICalculation iCalculation;

    @RequestMapping(value = "/calculation/{desc}", method = RequestMethod.GET)
    public int calculation(@PathVariable("desc") String desc){

        // TODO
        // 参数合法性判断

        return iCalculation.calculation(dmoBService.getId(desc));
    }
}
