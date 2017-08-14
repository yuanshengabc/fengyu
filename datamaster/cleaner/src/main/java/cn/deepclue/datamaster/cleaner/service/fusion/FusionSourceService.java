package cn.deepclue.datamaster.cleaner.service.fusion;

import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FromSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.OntologySourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.DatamasterSourceListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.ExternalDataSourceResqVO;
import cn.deepclue.datamaster.model.schema.RSSchema;

import java.util.List;
import java.util.Set;

/**
 * Created by magneto on 17-5-23.
 */
public interface FusionSourceService {
    List<FusionDataSourceBO> getFusionDataSourceBOs(Integer fwsid);

    List<FusionDatamasterSourceBO> getFusionDatamasterSourceBOs(Integer fwsid);

    DataTableListVO fetchDataTables(int dhid, String dbname, int page, int pageSize);

    DatamasterSourceListVO getDatamasterSources(int page, int pageSize);

    /**
     * 添加外部数据源
     *
     * @param externalDataSourceResqVOs 外部数据源
     * @return 返回数据源列表（包括添加过的和新添加的）
     */
    List<DataSource> insertDataSources(List<ExternalDataSourceResqVO> externalDataSourceResqVOs);

    List<DatamasterSourcePO> getDatamasterSources(List<Integer> datamasterSourceIds);

    /**
     * 添加关联
     *
     * @param fwsid               工作空间id
     * @param dataSources         外部数据源
     * @param datamasterSourcePOs 星河数据源
     * @return 返回新增的关联
     */
    List<WorkspaceSourcePO> insertWorkspaceSources(int fwsid, List<DataSource> dataSources, List<DatamasterSourcePO> datamasterSourcePOs);

    boolean insertOntologySources(List<OntologySourcePO> ontologySourcePOs);

    boolean removeOntologySource(int fwsid, int sid, SourceType sourceType);

    Integer removeSources(int fwsid);

    int getSources(int fwsid);

    boolean removeSource(int fwsid, int sid, SourceType sourceType);

    DatamasterSourcePO insertDatamasterSource(DatamasterSourcePO datamasterSourcePO);

    boolean saveRSSchema(Integer rsid, RSSchema rsSchema);

    List<OntologySourcePO> match(ObjectTypeBO objectTypeBO, List<WorkspaceSourcePO> workspaceSourcePOs,
                                 List<DataSource> dataSources, List<DatamasterSourcePO> datamasterSourcePOs);

    List<DataSourceBO> getAllDataSources(Integer dmsid);

    List<FromSourcePO> insertFromSources(List<FromSourcePO> allFromSourcePOs);

    Set<Integer> getMultiMatchPtids(int fwsid);
}
