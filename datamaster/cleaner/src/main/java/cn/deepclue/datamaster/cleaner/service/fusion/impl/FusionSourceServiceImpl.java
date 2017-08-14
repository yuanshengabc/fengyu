package cn.deepclue.datamaster.cleaner.service.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.WorkspaceSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DataHouseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DataSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.DatamasterSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FromSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.OntologySourceDao;
import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FromSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.OntologySourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.DatamasterSourceListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.ExternalDataSourceResqVO;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataSourceService;
import cn.deepclue.datamaster.cleaner.service.fusion.DatamasterSourceService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionSourceService;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by magneto on 17-5-23.
 */
@Service("sourceService")
public class FusionSourceServiceImpl implements FusionSourceService {
    @Autowired
    private DataHouseDao dataHouseDao;
    @Autowired
    DataSourceDao dataSourceDao;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    DatamasterSourceDao datamasterSourceDao;
    @Autowired
    DatamasterSourceService datamasterSourceService;
    @Autowired
    WorkspaceSourceDao workspaceSourceDao;
    @Autowired
    OntologySourceDao ontologySourceDao;
    @Autowired
    RecordSourceDao recordSourceDao;
    @Autowired
    FromSourceDao fromSourceDao;

    @Override
    public List<FusionDataSourceBO> getFusionDataSourceBOs(Integer fwsid) {
        return workspaceSourceDao.getFusionDataSources(fwsid);
    }

    @Override
    public List<FusionDatamasterSourceBO> getFusionDatamasterSourceBOs(Integer fwsid) {
        return workspaceSourceDao.getFusionDatamasterSources(fwsid);
    }

    @Override
    public DataTableListVO fetchDataTables(int dhid, String dbname, int page, int pageSize) {
        return dataSourceService.fetchDataTables(dhid, dbname, false, page, pageSize);
    }

    @Override
    public DatamasterSourceListVO getDatamasterSources(int page, int pageSize) {
        return datamasterSourceService.getDatamasterSources(page, pageSize);
    }

    /**
     * 添加外部数据源
     *
     * @param externalDataSourceResqVOs 外部数据源
     * @return 返回数据源列表（包括添加过的和新添加的）
     */
    @Override
    public List<DataSource> insertDataSources(List<ExternalDataSourceResqVO> externalDataSourceResqVOs) {
        List<DataSource> dataSources = new LinkedList<>();
        for (ExternalDataSourceResqVO externalDataSourceResqVO : externalDataSourceResqVOs) {
            int dhid = externalDataSourceResqVO.getDhid();
            String dbname = externalDataSourceResqVO.getDbname();
            String dtname = externalDataSourceResqVO.getDtname();
            DataSource dataSource = new DataSource(dhid, dbname, dtname);
            dataSourceDao.insertDataSource(dataSource);
            dataSources.add(dataSource);
        }
        return dataSources;
    }

    @Override
    public List<DatamasterSourcePO> getDatamasterSources(List<Integer> datamasterSourceIds) {
        if (datamasterSourceIds.isEmpty()) {
            return new ArrayList<>();
        }
        return datamasterSourceDao.getDatamasterSourcesByDsids(datamasterSourceIds);
    }

    /**
     * 添加关联
     *
     * @param fwsid               工作空间id
     * @param dataSources         外部数据源
     * @param datamasterSourcePOs 星河数据源
     * @return 返回新增的关联
     */
    @Override
    public List<WorkspaceSourcePO> insertWorkspaceSources(int fwsid, List<DataSource> dataSources, List<DatamasterSourcePO> datamasterSourcePOs) {
        List<WorkspaceSourcePO> allWorkspaceSourcePOs = new LinkedList<>();
        for (DataSource dataSource : dataSources) {
            WorkspaceSourcePO workspaceSourcePO = new WorkspaceSourcePO();
            workspaceSourcePO.setWsid(fwsid);
            workspaceSourcePO.setSid(dataSource.getDsid());
            workspaceSourcePO.setStype(SourceType.DATASOURCE.getType());
            workspaceSourcePO.setCreatedOn(new Date());
            allWorkspaceSourcePOs.add(workspaceSourcePO);
        }
        for (DatamasterSourcePO datamasterSourcePO : datamasterSourcePOs) {
            WorkspaceSourcePO workspaceSourcePO = new WorkspaceSourcePO();
            workspaceSourcePO.setWsid(fwsid);
            workspaceSourcePO.setSid(datamasterSourcePO.getDsid());
            workspaceSourcePO.setStype(SourceType.DATAMASTER_SOURCE.getType());
            workspaceSourcePO.setCreatedOn(new Date());
            allWorkspaceSourcePOs.add(workspaceSourcePO);
        }

        List<WorkspaceSourcePO> workspaceSourcePOs = new LinkedList<>();
        for (WorkspaceSourcePO tempWorkspaceSourcePO : allWorkspaceSourcePOs) {
            if (workspaceSourceDao.insertWorkspaceSource(tempWorkspaceSourcePO)) {
                workspaceSourcePOs.add(tempWorkspaceSourcePO);
            }
        }
        return workspaceSourcePOs;
    }

    @Override
    public boolean insertOntologySources(List<OntologySourcePO> ontologySourcePOs) {
        return ontologySourceDao.insertList(ontologySourcePOs);
    }

    @Override
    public boolean removeOntologySource(int fwsid, int sid, SourceType sourceType) {
        WorkspaceSourcePO workspaceSourcePO = workspaceSourceDao.getWorkspaceSource(fwsid, sid, sourceType.getType());
        return ontologySourceDao.delete(workspaceSourcePO.getWsdsid());
    }

    @Override
    public Integer removeSources(int fwsid) {
        return workspaceSourceDao.deleteByWsid(fwsid);
    }

    @Override
    public int getSources(int fwsid) {
        return workspaceSourceDao.getSourcesCount(fwsid);
    }

    @Override
    public boolean removeSource(int fwsid, int sid, SourceType sourceType) {
        return workspaceSourceDao.delete(fwsid, sid, sourceType.getType());
    }

    @Override
    public DatamasterSourcePO insertDatamasterSource(DatamasterSourcePO datamasterSourcePO) {
        return datamasterSourceDao.insertDatamasterSource(datamasterSourcePO);
    }

    @Override
    public boolean saveRSSchema(Integer rsid, RSSchema rsSchema) {
        recordSourceDao.saveRSSchema(rsid, rsSchema);
        return true;
    }

    @Override
    public List<OntologySourcePO> match(ObjectTypeBO objectTypeBO, List<WorkspaceSourcePO> workspaceSourcePOs,
                                        List<DataSource> dataSources, List<DatamasterSourcePO> datamasterSources) {
        List<OntologySourcePO> ontologySourcePOs = new LinkedList<>();
        for (WorkspaceSourcePO workspaceSourcePO : workspaceSourcePOs) {
            RSSchema rsSchema;

            int sid = workspaceSourcePO.getSid();
            SourceType sourceType = SourceType.getSourceType(workspaceSourcePO.getStype());
            if (SourceType.DATASOURCE.equals(sourceType)) {
                DataSource dataSource = findDataSource(sid, dataSources);
                rsSchema = fetchTableSchema(dataSource);
            } else if (SourceType.DATAMASTER_SOURCE.equals(sourceType)) {
                DatamasterSourcePO datamasterSource = findDatamasterSource(sid, datamasterSources);
                rsSchema = recordSourceDao.getRSSchema(datamasterSource.getRsid());
            } else {
                throw new IllegalStateException("stype not right");
            }

            ontologySourcePOs.add(matchDataSource(workspaceSourcePO, objectTypeBO, rsSchema));
        }
        return ontologySourcePOs;
    }

    @Override
    public List<DataSourceBO> getAllDataSources(Integer dmsid) {
        List<DataSourceBO> allDataSources = new ArrayList<>();
        return getAllDataSources(dmsid, allDataSources).stream().distinct().collect(toList());
    }

    @Override
    public List<FromSourcePO> insertFromSources(List<FromSourcePO> fromSourcePOs) {
        return fromSourceDao.insertFromSources(fromSourcePOs);
    }

    @Override
    public Set<Integer> getMultiMatchPtids(int fwsid) {
        List<String> multiMatchPtidsList = workspaceSourceDao.getMultiMatchPtids(fwsid);
        Set<Integer> multiMatchPtids = new HashSet<>();
        for (String item : multiMatchPtidsList) {
            String[] ptids = item.split(",");
            for (String ptid : ptids) {
                multiMatchPtids.add(Integer.valueOf(ptid));
            }
        }
        return multiMatchPtids;
    }

    private List<DataSourceBO> getAllDataSources(Integer dmsid, List<DataSourceBO> allDataSources) {
        List<FromSourcePO> fromSourcePOs = fromSourceDao.getFromSources(dmsid);

        List<DataSourceBO> dataSourceBOs = fromSourcePOs.stream().filter(po -> SourceType.getSourceType(po.getStype()) == SourceType.DATASOURCE).map(
                po -> dataSourceDao.getDataSourceBO(po.getFrom())
        ).collect(toList());

        if (dataSourceBOs != null && !dataSourceBOs.isEmpty()) {
            allDataSources.addAll(dataSourceBOs);
        }

        List<Integer> dmsids = fromSourcePOs.stream().filter(po -> SourceType.getSourceType(po.getStype()) == SourceType.DATAMASTER_SOURCE).map(po -> po.getFrom())
                .collect(toList());

        if (dmsids == null || dmsids.isEmpty()) {
            return allDataSources;
        }

        for (Integer fromDmsids : dmsids) {
            getAllDataSources(fromDmsids, allDataSources);
        }

        return allDataSources;
    }

    private DataSource findDataSource(int sid, List<DataSource> dataSources) {
        for (DataSource dataSource : dataSources) {
            if (sid == dataSource.getDsid()) {
                return dataSource;
            }
        }
        throw new IllegalStateException("sid not in dataSources");
    }

    private RSSchema fetchTableSchema(DataSource dataSource) {
        DataHouse dataHouse = dataHouseDao.getDataHouse(dataSource.getDhid());
        return dataSourceDao.fetchSchema(dataHouse, dataSource);
    }

    private DatamasterSourcePO findDatamasterSource(int sid, List<DatamasterSourcePO> datamasterSources) {
        for (DatamasterSourcePO datamasterSource : datamasterSources) {
            if (sid == datamasterSource.getDsid()) {
                return datamasterSource;
            }
        }
        throw new IllegalStateException("sid not in datamasterSources");
    }

    private OntologySourcePO matchDataSource(WorkspaceSourcePO workspaceSourcePO, ObjectTypeBO objectTypeBO, RSSchema rsSchema) {
        OntologySourcePO ontologySourcePO = new OntologySourcePO();
        ontologySourcePO.setWsdsid(workspaceSourcePO.getWsdsid());

        boolean match = true;
        Set<Integer> allMatchPtids = new HashSet<>();
        Set<Integer> multiMatchPtids = new HashSet<>();
        List<RSField> fields = rsSchema.getFields();
        List<PropertyType> propertyTypes = objectTypeBO.getPropertyTypes();
        for (RSField field : fields) {
            Integer matchPtid = matchField(propertyTypes, field);
            if (matchPtid == null) {
                match = false;
                break;
            } else {
                if (!allMatchPtids.contains(matchPtid)) {
                    allMatchPtids.add(matchPtid);
                } else {
                    multiMatchPtids.add(matchPtid);
                }
            }
        }
        ontologySourcePO.setMatch(match);
        if (match && !multiMatchPtids.isEmpty()) {
            ontologySourcePO.setMultimatchPtids(StringUtils.join(multiMatchPtids.toArray(), ','));
        }

        return ontologySourcePO;
    }

    private Integer matchField(List<PropertyType> propertyTypes, RSField field) {
        String fieldName = field.getName().replaceAll("\\d+$", StringUtils.EMPTY);
        for (PropertyType propertyType : propertyTypes) {
            String propertyTypeName = propertyType.getName().replaceAll("\\d+$", StringUtils.EMPTY);
            if (propertyTypeName.equals(fieldName) &&
                    propertyType.getBaseType().equals(field.getBaseTypeValue())) {
                return propertyType.getPtid();
            }
        }
        return null;
    }
}
