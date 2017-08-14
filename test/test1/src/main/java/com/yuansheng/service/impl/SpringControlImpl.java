package com.yuansheng.service.impl;

import com.yuansheng.dao.SpringControlDao;
import com.yuansheng.service.SpringControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("list")
public class SpringControlImpl implements SpringControlService{
    @Autowired
    private SpringControlDao springControlDao;

    @Override
    @Transactional
    public String getName(int id) {
        return springControlDao.getName(id);
    }
}
