package cn.deepclue.demo.appa.service.impl;

import cn.deepclue.demo.appa.dao.DemoDao;
import cn.deepclue.demo.appa.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    DemoDao demoDao;

    @Override
    public String getDesc(int id){

        return demoDao.getDescById(id);
    }
}
