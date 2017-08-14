package cn.deepclue.demo.appc.service.impl;

import cn.deepclue.demo.appc.service.CalculateService;
import org.springframework.stereotype.Service;

/**
 * Created by luoyong on 17-6-30.
 */
@Service
public class CalculateServiceImpl implements CalculateService {
    @Override
    public int calculation(int id) {
         return 100*id;
    }
}
