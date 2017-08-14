package cn.deepclue.datamaster.cleaner.web.fusion;

import cn.deepclue.datamaster.cleaner.domain.vo.fusion.FusionKeyDataSourceVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.FusionSourcesVO;
import cn.deepclue.datamaster.cleaner.service.fusion.DatamasterSourceService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by magneto on 17-6-6.
 */
@RestController
public class MagnetoController {
    @Autowired
    private DatamasterSourceService datamasterSourceService;

    @Autowired
    private FusionWorkspaceService fusionWorkspaceService;

    @RequestMapping(value = "/internal/fusionSources", method = RequestMethod.GET)
    public FusionSourcesVO getFusionSources() {
        return datamasterSourceService.getFusionSources();
    }

    /**
     * 返回所有的外部数据源
     * @param dmsid
     * @return
     */
    @RequestMapping(value = "/internal/fusionSources/{dmsid}/dataSources", method = RequestMethod.GET)
    public List<FusionKeyDataSourceVO> getAllDataSources(@PathVariable("dmsid") Integer dmsid) {
        return fusionWorkspaceService.getAllDataSources(dmsid);
    }
}
