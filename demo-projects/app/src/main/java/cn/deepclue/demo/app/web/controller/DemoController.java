package cn.deepclue.demo.app.web.controller;


import cn.deepclue.demo.app.common.log.HttpLogIgnore;
import cn.deepclue.demo.app.domain.bo.DemoBO;
import cn.deepclue.demo.app.domain.vo.DemoAddVO;
import cn.deepclue.demo.app.domain.vo.DemoVO;
import cn.deepclue.demo.app.service.DemoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by luoyong on 17-6-30.
 */
@RestController
@RequestMapping("/demo")
public class
DemoController {

    @Autowired
    private DemoService demoService;

    @HttpLogIgnore
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public DemoVO getById(@PathVariable("id") Long id) {
        DemoBO bo = this.demoService.getDemoById(id);
        DemoVO vo = new DemoVO();
        BeanUtils.copyProperties(bo, vo);
        return vo;
    }

    @RequestMapping(method = RequestMethod.POST)
    public int add(@Valid DemoAddVO vo) {
        return this.demoService.add(vo);
    }
}
