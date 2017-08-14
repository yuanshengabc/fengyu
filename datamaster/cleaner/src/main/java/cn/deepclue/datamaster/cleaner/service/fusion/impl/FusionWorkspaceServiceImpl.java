package cn.deepclue.datamaster.cleaner.service.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.TaskDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataTable;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.fusion.EntropyCalculationStatus;
import cn.deepclue.datamaster.cleaner.domain.fusion.FusionTaskStatus;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.EntropyPropertyPO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FromSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.OntologySourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.*;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionSourceService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionStepService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionWorkspaceService;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next.FusionNextStep;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous.FusionPreviousStep;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by magneto on 17-5-15.
 */
@Service("fusionWorkspaceService")
public class FusionWorkspaceServiceImpl implements FusionWorkspaceService {
    @Autowired
    private FusionWorkspaceDao fusionWorkspaceDao;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private FusionStepService fusionStepService;
    @Autowired
    private EntropyService entropyService;
    @Autowired
    private FusionSourceService fusionSourceService;

    @Override
    @Transactional
    public boolean nextStep(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        if (fusionWorkspaceBO == null) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_INFO_INCOMPLETE);
        }

        FusionNextStep fusionNextStep = fusionStepService.currentNextStep(fusionWorkspaceBO);

        FusionStep nextStep = fusionNextStep.next();

        //update step
        fusionWorkspaceDao.updateStep(fwsid, nextStep.getStep());

        return true;
    }

    @Override
    @Transactional
    public boolean previousStep(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        if (fusionWorkspaceBO == null) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_INFO_INCOMPLETE);
        }

        FusionPreviousStep fusionPreviousStep = fusionStepService.currentPreviousStep(fusionWorkspaceBO);

        FusionStep fusionStep = fusionPreviousStep.previous();

        //update step
        fusionWorkspaceDao.updateStep(fwsid, fusionStep.getStep());

        return true;
    }

    @Override
    public FusionObjectTypeVO getSelectedObjectType(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        ObjectTypeBO objectTypeBO = fusionWorkspaceBO.getObjectTypeBO();
        if (objectTypeBO == null) {
            return null;
        }
        return new FusionObjectTypeVO().fromBO(objectTypeBO);
    }

    @Override
    public boolean selectObjectType(int fwsid, int otid) {
        return fusionWorkspaceDao.updateObjectType(fwsid, otid);
    }

    @Override
    public List<FusionDataSourceVO> getSelectedDataSources(int fwsid) {
        List<FusionDataSourceVO> fusionDataSourceVOs = new LinkedList<>();

        //添加外部数据源
        List<FusionDataSourceBO> fusionDataSourceBOs = fusionSourceService.getFusionDataSourceBOs(fwsid);
        for (FusionDataSourceBO fusionDataSourceBO : fusionDataSourceBOs) {
            fusionDataSourceVOs.add(new FusionDataSourceVO().fromBO(fusionDataSourceBO));
        }

        //添加内部数据源
        List<FusionDatamasterSourceBO> fusionDatamasterSourceBOs = fusionSourceService.getFusionDatamasterSourceBOs(fwsid);
        for (FusionDatamasterSourceBO fusionDatamasterSourceBO : fusionDatamasterSourceBOs) {
            fusionDataSourceVOs.add(new FusionDataSourceVO().fromBO(fusionDatamasterSourceBO));
        }

        //排序
        fusionDataSourceVOs.sort(Comparator.comparing(FusionDataSourceVO::getCreatedOn));
        return fusionDataSourceVOs;
    }

    @Override
    public FusionExternalDataSourceListVO fetchExternalDataSources(int fwsid, int dhid, String dbname, int page, int pageSize) {
        //获取已选择的外部数据源
        List<FusionDataSourceBO> selectedDataSources = fusionSourceService.getFusionDataSourceBOs(fwsid);

        //分页获取数据库中的表
        DataTableListVO dataTableList = fusionSourceService.fetchDataTables(dhid, dbname, page, pageSize);

        //得到VO
        FusionExternalDataSourceListVO fusionExternalDataSourceList = new FusionExternalDataSourceListVO();
        fusionExternalDataSourceList.setDsCount(dataTableList.getDsCount());
        List<FusionExternalDataSourceVO> externalDataSources = new LinkedList<>();
        for (DataTable dataTable : dataTableList.getDataTables()) {
            FusionExternalDataSourceVO externalDataSourceVO = new FusionExternalDataSourceVO().from(dataTable, selectedDataSources);
            externalDataSources.add(externalDataSourceVO);
        }
        fusionExternalDataSourceList.setExternalDataSources(externalDataSources);
        return fusionExternalDataSourceList;
    }

    @Override
    public FusionDatamasterSourceListVO getDatamasterSources(int fwsid, int page, int pageSize) {
        //获取已选择的星河数据源
        List<FusionDatamasterSourceBO> selectedDatamasterSources = fusionSourceService.getFusionDatamasterSourceBOs(fwsid);

        //分页获取星河数据源
        DatamasterSourceListVO datamasterSourceList = fusionSourceService.getDatamasterSources(page, pageSize);

        //得到VO
        FusionDatamasterSourceListVO fusionDatamasterSourceList = new FusionDatamasterSourceListVO();
        fusionDatamasterSourceList.setDsCount(datamasterSourceList.getDsCount());
        List<FusionDatamasterSourceVO> fusionDatamasterSources = new LinkedList<>();
        for (DatamasterSourcePO datamasterSource : datamasterSourceList.getDatamasterSources()) {
            FusionDatamasterSourceVO fusionDatamasterSource = new FusionDatamasterSourceVO().from(datamasterSource, selectedDatamasterSources);
            fusionDatamasterSources.add(fusionDatamasterSource);
        }
        fusionDatamasterSourceList.setDatamasterSources(fusionDatamasterSources);
        return fusionDatamasterSourceList;
    }

    @Transactional
    @Override
    public boolean addDataSources(int fwsid, DataSourcesResqVO dataSourcesResqVO) {
        //插入外部数据源，并返回
        List<DataSource> dataSources = fusionSourceService.insertDataSources(dataSourcesResqVO.getExternalDataSources());

        //获取星河数据源
        List<DatamasterSourcePO> datamasterSourcePOs = fusionSourceService.getDatamasterSources(dataSourcesResqVO.getDatamasterSources());

        //添加关联，并返回新增的关联
        List<WorkspaceSourcePO> workspaceSourcePOs = fusionSourceService.insertWorkspaceSources(fwsid, dataSources, datamasterSourcePOs);

        if (!workspaceSourcePOs.isEmpty()) {
            //判断是否匹配
            FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
            List<OntologySourcePO> ontologySourcePOs = fusionSourceService.match(fusionWorkspaceBO.getObjectTypeBO(), workspaceSourcePOs, dataSources, datamasterSourcePOs);

            //保存匹配状态
            fusionSourceService.insertOntologySources(ontologySourcePOs);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean deleteDataSource(int fwsid, int dsid, String type) {
        fusionSourceService.removeOntologySource(fwsid, dsid, SourceType.valueOf(type));
        fusionSourceService.removeSource(fwsid, dsid, SourceType.valueOf(type));
        return true;
    }

    @Override
    public FusionInfoVO getFusionInfo(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        return new FusionInfoVO().from(fusionWorkspaceBO);
    }

    @Override
    public EntropyTableVO getEntropyTable(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        ObjectTypeBO objectTypeBO = fusionWorkspaceBO.getObjectTypeBO();
        if (objectTypeBO == null) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_INFO_INCOMPLETE);
        }
        Set<Integer> multiMatchPtids = fusionSourceService.getMultiMatchPtids(fwsid);
        EntropyTableBO entropyTableBO = entropyService.getEntropyTable(fwsid);
        return new EntropyTableVO().fromBO(objectTypeBO, entropyTableBO, multiMatchPtids);
    }

    @Override
    public boolean updateFusionStatus(int fwsid, String addressCodeType) {
        fusionWorkspaceDao.updateAddressCodeType(fwsid, AddressCodeType.valueOf(addressCodeType));
        //切换码址类型时，需清除其它设置信息
        fusionWorkspaceDao.updateThreshold(fwsid, null);
        entropyService.deleteEntropyTable(fwsid);
        return true;
    }

    @Override
    public boolean startEntropyCalculation(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        Task task = taskDao.createEntropyComputeTask(fusionWorkspaceBO);
        fusionWorkspaceDao.updateEntropyCalculationTask(fwsid, task.getTid());
        taskScheduler.schedule(task);

        return true;
    }

    @Transactional
    @Override
    public boolean updateEntropyFields(int fwsid, String addressCodeType, List<Integer> selectedPtids, Double threshold, Integer uniquePtid) {
        AddressCodeType addressCodeTypeEnum = AddressCodeType.valueOf(addressCodeType);
        fusionWorkspaceDao.updateAddressCodeType(fwsid, addressCodeTypeEnum);
        fusionWorkspaceDao.updateThreshold(fwsid, threshold);
        entropyService.deleteEntropyTable(fwsid);

        List<EntropyPropertyPO> entropyPropertyPOs = new LinkedList<>();
        for (Integer selectedPtid : selectedPtids) {
            EntropyPropertyPO entropyPropertyPO = new EntropyPropertyPO();
            entropyPropertyPO.setWsid(fwsid);
            entropyPropertyPO.setPtid(selectedPtid);
            //单码址默认设置唯一列为true
            if (selectedPtid.equals(uniquePtid) || AddressCodeType.SINGLE_ADDRESS_CODE.equals(addressCodeTypeEnum)) {
                entropyPropertyPO.setUnique(true);
            } else {
                entropyPropertyPO.setUnique(false);
            }

            entropyPropertyPOs.add(entropyPropertyPO);
        }
        entropyService.insertEntropyTable(entropyPropertyPOs);
        return true;
    }

    @Override
    public FusionTaskStatusInfoVO getFusionTaskStatusInfo(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        AddressCodeType addressCodeType = fusionWorkspaceBO.getAddressCodeType();
        FusionTaskStatus status = fusionWorkspaceBO.getStatus();

        EntropyTableBO entropyTableBO = null;
        if (AddressCodeType.MULTI_ADDRESS_CODE.equals(addressCodeType)
                && EntropyCalculationStatus.CALCULATED.equals(fusionWorkspaceBO.getEntropyCalculationStatus())) {
            entropyTableBO = entropyService.getSelectedEntropyTable(fwsid);
        }

        PreviewStats previewStats = null;
        RecordSource recordSource = fusionWorkspaceBO.getRecordSource();
        if (recordSource != null && AddressCodeType.MULTI_ADDRESS_CODE.equals(addressCodeType)
                && FusionTaskStatus.CALCULATED.equals(status)) {
            previewStats = entropyService.getFusionPreviewInfo(recordSource);
        }

        Long fieldFusionNum = null;
        if (recordSource != null && FusionTaskStatus.CALCULATED.equals(status)) {
            if (AddressCodeType.SINGLE_ADDRESS_CODE.equals(addressCodeType)) {
                fieldFusionNum = entropyService.getSingleFieldFusionNum(recordSource);
            } else if (AddressCodeType.MULTI_ADDRESS_CODE.equals(addressCodeType)) {
                fieldFusionNum = entropyService.getMultiFieldFusionNum(recordSource);
            }
        }
        return new FusionTaskStatusInfoVO().from(fusionWorkspaceBO, entropyTableBO, previewStats, fieldFusionNum);
    }

    @Override
    @Transactional
    public DatamasterSourcePO saveFusionResult(int fwsid, String name, String description) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        Integer rsid = fusionWorkspaceBO.getRecordSource().getRsid();

        ObjectTypeBO objectTypeBO = fusionWorkspaceBO.getObjectTypeBO();
        RSSchema rsSchema = objectTypeBO.toRsSchema();
        fusionSourceService.saveRSSchema(rsid, rsSchema);

        DatamasterSourcePO datamasterSourcePO = new DatamasterSourcePO();
        datamasterSourcePO.setName(name);
        datamasterSourcePO.setDescription(description);
        datamasterSourcePO.setRsid(rsid);
        datamasterSourcePO.setSource(fusionWorkspaceBO.getName());
        datamasterSourcePO.setCreatedOn(new Date());
        fusionSourceService.insertDatamasterSource(datamasterSourcePO);

        //save from source
        List<FusionDataSourceBO> fusionDataSourceBOs = fusionSourceService.getFusionDataSourceBOs(fwsid);
        List<FromSourcePO> fromSourcePOsOfDataSource = fusionDataSourceBOs.stream().map(
                fusionDataSourceBO -> {
                    FromSourcePO fromSourcePO = new FromSourcePO();
                    fromSourcePO.setDsid(datamasterSourcePO.getDsid());
                    fromSourcePO.setFrom(fusionDataSourceBO.getDataSourceBO().getDsid());
                    fromSourcePO.setStype(SourceType.DATASOURCE.getType());

                    return fromSourcePO;
                }
        ).collect(toList());

        List<FusionDatamasterSourceBO> fusionDatamasterSourceBOs = fusionSourceService.getFusionDatamasterSourceBOs(fwsid);
        List<FromSourcePO> fromSourcePOsOfDatamasterSource = fusionDatamasterSourceBOs.stream().map(
                fusionDatamasterSourceBO -> {
                    FromSourcePO fromSourcePO = new FromSourcePO();
                    fromSourcePO.setDsid(datamasterSourcePO.getDsid());
                    fromSourcePO.setFrom(fusionDatamasterSourceBO.getDatamasterSourcePO().getDsid());
                    fromSourcePO.setStype(SourceType.DATAMASTER_SOURCE.getType());

                    return fromSourcePO;
                }
        ).collect(toList());

        List<FromSourcePO> allFromSourcePOs = new ArrayList<>();
        allFromSourcePOs.addAll(fromSourcePOsOfDataSource);
        allFromSourcePOs.addAll(fromSourcePOsOfDatamasterSource);

        fusionSourceService.insertFromSources(allFromSourcePOs);

        return datamasterSourcePO;
    }

    @Override
    public List<FusionKeyDataSourceVO> getAllDataSources(Integer dmsid) {
        List<DataSourceBO> fusionKeyDataSourceBOs = fusionSourceService.getAllDataSources(dmsid);

        List<FusionKeyDataSourceVO> fusionKeyDataSourceVOs = fusionKeyDataSourceBOs.stream().filter(bo -> bo != null).map(
                bo -> {
                    FusionKeyDataSourceVO fusionKeyDataSourceVO = new FusionKeyDataSourceVO();

                    BeanUtils.copyProperties(bo, fusionKeyDataSourceVO);
                    DataHouse dataHouse = bo.getDataHouse();
                    BeanUtils.copyProperties(dataHouse, fusionKeyDataSourceVO);

                    return fusionKeyDataSourceVO;
                }
        ).collect(toList());

        return fusionKeyDataSourceVOs;
    }

}
