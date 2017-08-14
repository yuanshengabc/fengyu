package cn.deepclue.datamaster.cleaner.service.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.*;

import java.util.List;

/**
 * Created by magneto on 17-5-15.
 */
public interface FusionWorkspaceService {
    boolean nextStep(int fwsid);

    boolean previousStep(int fwsid);

    FusionObjectTypeVO getSelectedObjectType(int fwsid);

    boolean selectObjectType(int fwsid, int otid);

    List<FusionDataSourceVO> getSelectedDataSources(int fwsid);

    FusionExternalDataSourceListVO fetchExternalDataSources(int fwsid, int dhid, String dbname, int page, int pageSize);

    FusionDatamasterSourceListVO getDatamasterSources(int fwsid, int page, int pageSize);

    boolean addDataSources(int fwsid, DataSourcesResqVO dataSourcesResqVO);

    boolean deleteDataSource(int fwsid, int dsid, String type);

    FusionInfoVO getFusionInfo(int fwsid);

    EntropyTableVO getEntropyTable(int fwsid);

    boolean updateFusionStatus(int fwsid, String addressCodeType);

    boolean startEntropyCalculation(int fwsid);

    boolean updateEntropyFields(int fwsid, String addressCodeType, List<Integer> ptids, Double threshold, Integer uniquePtid);

    FusionTaskStatusInfoVO getFusionTaskStatusInfo(int fwsid);

    DatamasterSourcePO saveFusionResult(int fwsid, String name, String description);

    List<FusionKeyDataSourceVO> getAllDataSources(Integer dmsid);
}
