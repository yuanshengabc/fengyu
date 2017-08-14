package cn.deepclue.demo.appb.service.feign.hystric;

import cn.deepclue.demo.appb.service.feign.ICalculation;
import org.springframework.stereotype.Component;

@Component
public class ICalculationHystric implements ICalculation {
    @Override
    public int calculation(int id) {
        return 10*id;
    }
}
