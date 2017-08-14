package cn.deepclue.datamaster.cleaner.web.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.*;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionWorkspaceService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by magneto on 17-5-15.
 */
@RestController
public class FusionWorkspaceController {
    @Autowired
    private FusionWorkspaceService fusionWorkspaceService;

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/nextStep", method = RequestMethod.POST)
    public boolean nextStep(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.nextStep(fwsid);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/previousStep", method = RequestMethod.POST)
    public boolean previousStep(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.previousStep(fwsid);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/objectType", method = RequestMethod.GET)
    public FusionObjectTypeVO getSelectedObjectType(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.getSelectedObjectType(fwsid);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/objectType", method = RequestMethod.POST)
    public boolean selectObjectType(@PathVariable("fwsid") int fwsid, @RequestParam("otid") int otid) {
        return fusionWorkspaceService.selectObjectType(fwsid, otid);
    }

    /**
     * 获取所有已选择的数据源，包括外部数据源和星河数据源
     *
     * @param fwsid 融合工作空间id
     * @return 所有已选择的数据源
     */
    @RequestMapping(path = "/fusionWorkspace/{fwsid}/dataSources", method = RequestMethod.GET)
    public List<FusionDataSourceVO> getSelectedDataSources(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.getSelectedDataSources(fwsid);
    }

    /**
     * 分页获取数据库中的表，同时添加是否已添加标记
     *
     * @param fwsid    融合工作空间id
     * @param dhid     数据仓库id
     * @param dbname   数据库名称
     * @param page     页码
     * @param pageSize 分页大小
     * @return 表信息以及是否已添加
     */
    @RequestMapping(path = "/fusionWorkspace/{fwsid}/externalDataSources", method = RequestMethod.GET)
    public FusionExternalDataSourceListVO fetchExternalDataSources(@PathVariable("fwsid") int fwsid,
                                                                   @RequestParam("dhid") int dhid,
                                                                   @RequestParam("dbname") String dbname,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @Range(min = 10, max = 50) @RequestParam(defaultValue = "10") int pageSize) {
        return fusionWorkspaceService.fetchExternalDataSources(fwsid, dhid, dbname, page, pageSize);
    }

    /**
     * 分页获取星河数据源，同时添加是否已添加标记
     *
     * @param fwsid    融合工作空间id
     * @param page     页码
     * @param pageSize 分页大小
     * @return 星河数据源信息以及是否已添加
     */
    @RequestMapping(path = "/fusionWorkspace/{fwsid}/datamasterSources", method = RequestMethod.GET)
    public FusionDatamasterSourceListVO getDatamasterSources(@PathVariable("fwsid") int fwsid,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @Range(min = 10, max = 50) @RequestParam(defaultValue = "10") int pageSize) {
        return fusionWorkspaceService.getDatamasterSources(fwsid, page, pageSize);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/dataSources", method = RequestMethod.POST)
    public boolean addDataSources(@PathVariable("fwsid") int fwsid,
                                  @RequestBody DataSourcesResqVO dataSourcesResqVO) {
        return fusionWorkspaceService.addDataSources(fwsid, dataSourcesResqVO);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/dataSources", method = RequestMethod.DELETE)
    public boolean deleteDataSource(@PathVariable("fwsid") int fwsid,
                                    @RequestParam("dsid") int dsid,
                                    @RequestParam("type") String type) {
        return fusionWorkspaceService.deleteDataSource(fwsid, dsid, type);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/fusionStatus", method = RequestMethod.GET)
    public FusionInfoVO getFusionInfo(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.getFusionInfo(fwsid);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/entropyFields", method = RequestMethod.GET)
    public EntropyTableVO getEntoryTable(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.getEntropyTable(fwsid);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/fusionStatus", method = RequestMethod.POST)
    public boolean updateFusionStatus(@PathVariable("fwsid") int fwsid, @RequestParam("addressCodeType") String addressCodeType) {
        return fusionWorkspaceService.updateFusionStatus(fwsid, addressCodeType);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/entropyCalculation", method = RequestMethod.POST)
    public boolean startEntropyCalculation(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.startEntropyCalculation(fwsid);
    }

    /**
     * 提交融合方式设置
     *
     * @param fwsid           融合工作空间id
     * @param addressCodeType 码址类型
     * @param ptids           选择的属性列
     * @param threshold       相似度阀值
     * @param uniquePtid     设置的唯一列
     * @return 是否保存成功
     */
    @RequestMapping(path = "/fusionWorkspace/{fwsid}/entropyFields", method = RequestMethod.POST)
    public boolean updateEntropyFields(@PathVariable("fwsid") int fwsid,
                                       @RequestParam("addressCodeType") String addressCodeType,
                                       @RequestParam("ptids") List<Integer> ptids,
                                       @RequestParam("threshold") Double threshold,
                                       @RequestParam("uniquePtid") Integer uniquePtid) {
        return fusionWorkspaceService.updateEntropyFields(fwsid, addressCodeType, ptids, threshold, uniquePtid);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/fusionTaskStatusInfo", method = RequestMethod.GET)
    public FusionTaskStatusInfoVO getFusionTaskStatusInfo(@PathVariable("fwsid") int fwsid) {
        return fusionWorkspaceService.getFusionTaskStatusInfo(fwsid);
    }

    @RequestMapping(path = "/fusionWorkspace/{fwsid}/save", method = RequestMethod.POST)
    public DatamasterSourcePO saveFusionResult(@PathVariable("fwsid") int fwsid, @RequestParam("name") String name, @RequestParam("description") String description) {
        return fusionWorkspaceService.saveFusionResult(fwsid, name, description);
    }
}
