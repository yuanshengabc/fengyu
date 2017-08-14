package cn.deepclue.demo.app.service;

import cn.deepclue.demo.app.domain.bo.DemoBO;
import cn.deepclue.demo.app.domain.vo.DemoAddVO;
import cn.deepclue.demo.app.domain.vo.DemoVO;

/**
 * Created by luoyong on 17-6-26.
 */
public interface DemoService {
    DemoBO getDemoById(Long id);

    int add(DemoAddVO vo);
}
