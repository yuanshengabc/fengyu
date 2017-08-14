package cn.deepclue.demo.appb.service.impl;

import cn.deepclue.demo.appb.dao.DemoDao;
import cn.deepclue.demo.appb.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService{

    @Autowired
    private DemoDao demoDao;

    @Override
    public int getId(String desc){

        return demoDao.getIdbyDescribe(desc);
    }
}
