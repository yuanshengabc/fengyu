package cn.deepclue.demo.appb.service.feign;

import cn.deepclue.demo.appb.service.feign.hystric.ICalculationHystric;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "app-c", fallback = ICalculationHystric.class)
public interface ICalculation {

    @RequestMapping(value = "/calculation/{id}",method = RequestMethod.GET)
    int calculation(@RequestParam(value = "id") int id);
}
