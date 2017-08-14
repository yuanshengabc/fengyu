package cn.deepclue.demo.app.service.impl;

import cn.deepclue.demo.app.common.exception.AppErrorEnum;
import cn.deepclue.demo.app.common.exception.AppException;
import cn.deepclue.demo.app.dao.DemoDao;
import cn.deepclue.demo.app.domain.bo.DemoBO;
import cn.deepclue.demo.app.domain.po.DemoPO;
import cn.deepclue.demo.app.domain.vo.DemoAddVO;
import cn.deepclue.demo.app.service.DemoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by luoyong on 17-6-26.
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDao demoDao;

    @Override
    public DemoBO getDemoById(Long id) {
        DemoPO po = this.demoDao.getById(id);
        if (po == null) {
            throw new AppException(AppErrorEnum.DEMO_NOT_EXISTS);
        }
        DemoBO bo = new DemoBO();
        BeanUtils.copyProperties(po, bo);
        return bo;
    }

    @Override
    public int add(DemoAddVO vo) {
        DemoPO demoPO = new DemoPO();
        BeanUtils.copyProperties(vo, demoPO);
        demoPO.setCreateTime(new Date());
        demoPO.setUpdateTime(new Date());
        return demoDao.insert(demoPO);
    }
}
