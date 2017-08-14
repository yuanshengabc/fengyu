package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.dao.WorkspaceDao;
import cn.deepclue.datamaster.cleaner.dao.WorkspaceSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.*;
import cn.deepclue.datamaster.cleaner.dao.fusion.DatamasterSourceDao;
import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.WorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEditionStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.CleaningWorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.WorkspaceEditionPO;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.CreateWorkspaceReqVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceListRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.CleaningWorkspaceVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionWorkspaceVO;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by magneto on 17-5-15.
 */
@Service("workspaceService")
public class WorkspaceServiceImpl implements WorkspaceService {
    private static Logger logger = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private WorkspaceDao workspaceDao;
    @Autowired
    private CleaningWorkspaceDao cleaningWorkspaceDao;
    @Autowired
    private DataSourceDao dataSourceDao;
    @Autowired
    private DatamasterSourceDao datamasterSourceDao;
    @Autowired
    private WorkspaceSourceDao workspaceSourceDao;
    @Autowired
    private RecordSourceDao recordSourceDao;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private DataHouseDao dataHouseDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private EntropyService entropyService;

    private WorkspaceListRespVO createWorkspaceListRespDTO(List<WorkspaceBO> workspaceBOs, FinishedStatus status, String keyword) {
        WorkspaceListRespVO workspaceListRespVO = new WorkspaceListRespVO();
        workspaceListRespVO.setWscount(workspaceDao.getWorkspacesCount(null, status, keyword));
        workspaceListRespVO.setCleaningCount(workspaceDao.getWorkspacesCount(WorkspaceType.CLEANING, status, keyword));
        workspaceListRespVO.setFusionCount(workspaceDao.getWorkspacesCount(WorkspaceType.FUSION, status, keyword));
        List<WorkspaceVO> workspaceVOs = new ArrayList<>();
        if (workspaceBOs != null) {
            for (WorkspaceBO workspaceBO : workspaceBOs) {
                WorkspaceVO workspaceVO;
                if (workspaceBO.getWstype() == WorkspaceType.CLEANING) {
                    CleaningWorkspaceBO cleaningWorkspaceBO = (CleaningWorkspaceBO) workspaceBO;
                    CleaningWorkspaceVO cleaningWorkspaceVO = new CleaningWorkspaceVO();
                    cleaningWorkspaceVO.fromBO(cleaningWorkspaceBO);
                    workspaceVO = cleaningWorkspaceVO;
                } else if (workspaceBO.getWstype() == WorkspaceType.FUSION) {
                    FusionWorkspaceBO fusionWorkspaceBO = (FusionWorkspaceBO) workspaceBO;
                    FusionWorkspaceVO fusionWorkspaceVO = new FusionWorkspaceVO();
                    fusionWorkspaceVO.fromBO(fusionWorkspaceBO);
                    workspaceVO = fusionWorkspaceVO;
                } else {
                    throw new JdbcException(JdbcErrorEnum.SELECT);
                }
                workspaceVOs.add(workspaceVO);
            }
        }

        workspaceListRespVO.setWorkspaces(workspaceVOs);

        return workspaceListRespVO;
    }

    @Override
    public WorkspaceListRespVO getWorkspaces(int page, int pageSize, WorkspaceType wstype, FinishedStatus status, String keyword) {
        List<WorkspaceBO> workspaceBOs = workspaceDao.getWorkspaces(page, pageSize, wstype, status, keyword);
        return createWorkspaceListRespDTO(workspaceBOs, status, keyword);
    }

    @Override
    @Transactional
    public int createWorkspace(CreateWorkspaceReqVO workspaceReqVO) {
        if (workspaceReqVO.getWstype() == WorkspaceType.CLEANING) {
            return createCleaningWorkspace(workspaceReqVO);
        }

        return createFusionWorkspace(workspaceReqVO);
    }

    @Transactional
    public int createFusionWorkspace(CreateWorkspaceReqVO workspaceReqVO) {
        // Insert workspace po
        WorkspacePO workspacePO = new WorkspacePO();
        workspacePO.setCreatedOn(new Date());
        workspacePO.setModifiedOn(new Date());
        workspacePO.setName(workspaceReqVO.getName());
        workspacePO.setDescription(workspaceReqVO.getDescription());
        workspacePO.setFinished(FinishedStatus.UNFINISHED.getValue());
        workspacePO.setWstype(WorkspaceType.FUSION.getType());

        workspaceDao.insertWorkspace(workspacePO);

        // Create record source
        RecordSource recordSource = recordSourceDao.createRecordSource(workspaceReqVO.getName(), RecordSource.RSType.FUSION);

        // Insert fusion workspace po
        FusionWorkspacePO fusionWorkspacePO = new FusionWorkspacePO();
        fusionWorkspacePO.setWsid(workspacePO.getWsid());
        fusionWorkspacePO.setStep(FusionStep.ONTOLOGY_SELECTION.getStep());
        fusionWorkspacePO.setRsid(recordSource.getRsid());

        workspaceDao.insertWorkspace(fusionWorkspacePO);

        return workspacePO.getWsid();
    }

    @Transactional
    public int createCleaningWorkspace(CreateWorkspaceReqVO workspaceReqVO) {
        DataHouse dataHouse = dataHouseDao.getDataHouse(workspaceReqVO.getDhid());
        if (dataHouse == null) {
            throw new JdbcException("Failed to fetch data house by dhid: " + workspaceReqVO.getDhid(),
                    "根据数据仓库id: " + workspaceReqVO.getDhid() + "获取数据仓库失败。",
                    JdbcErrorEnum.SELECT);
        }

        // Prepare data source and record source.
        DataSource dataSource = new DataSource(workspaceReqVO.getDhid(), workspaceReqVO.getDbname(), workspaceReqVO.getDtname());
        dataSourceDao.insertDataSource(dataSource);
        RecordSource recordSource = recordSourceDao.createRecordSource(dataSource);

        // Bind dsid -> rsid
        dataSource.setRsid(recordSource.getRsid());
        dataSourceDao.updateDataSource(dataSource);

        RSSchema schema = dataSourceDao.fetchSchema(dataHouse, dataSource);
        recordSourceDao.saveRSSchema(recordSource.getRsid(), schema);

        // Create workspace
        WorkspacePO workspacePO = new WorkspacePO();
        BeanUtils.copyProperties(workspaceReqVO, workspacePO);
        workspacePO.setCreatedOn(new Date());
        workspacePO.setModifiedOn(new Date());
        workspacePO.setWstype(WorkspaceType.CLEANING.getType());
        workspacePO.setFinished(FinishedStatus.UNFINISHED.getValue());
        workspaceDao.insertWorkspace(workspacePO);

        CleaningWorkspacePO cleaningWorkspacePO = new CleaningWorkspacePO();
        cleaningWorkspacePO.setWsid(workspacePO.getWsid());
        cleaningWorkspacePO.setWsversion(0);

        workspaceDao.insertWorkspace(cleaningWorkspacePO);

        // Bind wsid -> dsid
        WorkspaceSourcePO workspaceSourcePO = new WorkspaceSourcePO();
        workspaceSourcePO.setWsid(workspacePO.getWsid());
        workspaceSourcePO.setSid(dataSource.getDsid());
        workspaceSourcePO.setStype(SourceType.DATASOURCE.getType());
        workspaceSourceDao.insertWorkspaceSource(workspaceSourcePO);

        // Prepare a task to import data source to record source.
        Task task = taskDao.createImportTask(dataSource, recordSource);

        Task analysisTask = taskDao.createAnalysisTask(recordSource);

        // Create workspace edition
        WorkspaceEditionPO workspaceEditionPO = new WorkspaceEditionPO();
        workspaceEditionPO.setModifiedOn(new Date());
        workspaceEditionPO.setRsid(recordSource.getRsid());
        workspaceEditionPO.setWsversion(0);
        workspaceEditionPO.setWsid(workspacePO.getWsid());
        workspaceEditionPO.setWorkspaceStatus(WorkspaceEditionStatus.UNCLEANED);
        workspaceEditionPO.setImportTask(task);
        workspaceEditionPO.setAnalysisTask(analysisTask);
        cleaningWorkspaceDao.insertWorkspaceEdition(workspaceEditionPO);

        // Submit import task to task scheduler server.
        taskScheduler.schedule(task);

        // Submit analysis task.
        taskScheduler.schedule(analysisTask);

        return workspacePO.getWsid();
    }

    @Override
    public WorkspaceVO getWorkspace(int wsid) {
        WorkspaceBO workspaceBO = workspaceDao.getWorkspace(wsid);
        WorkspaceVO workspaceVO = null;
        if (workspaceBO.getWstype() == WorkspaceType.CLEANING) {
            CleaningWorkspaceBO cleaningWorkspaceBO = (CleaningWorkspaceBO) workspaceBO;
            CleaningWorkspaceVO cleaningWorkspaceVO = new CleaningWorkspaceVO();
            cleaningWorkspaceVO.fromBO(cleaningWorkspaceBO);
            workspaceVO = cleaningWorkspaceVO;

        } else {
            FusionWorkspaceBO fusionWorkspaceBO = (FusionWorkspaceBO) workspaceBO;
            FusionWorkspaceVO fusionWorkspaceVO = new FusionWorkspaceVO();
            fusionWorkspaceVO.fromBO(fusionWorkspaceBO);
            workspaceVO = fusionWorkspaceVO;
        }

        return workspaceVO;
    }

    @Override
    public boolean updateWorkspace(int wsid, String name, String description) {
        return workspaceDao.updateWorkspace(wsid, name, description);
    }

    @Override
    @Transactional
    public boolean deleteWorkspace(int wsid) {
        WorkspaceBO workspaceBO = workspaceDao.getWorkspace(wsid);
        if (workspaceBO.getWstype() == WorkspaceType.CLEANING) {
            CleaningWorkspaceBO cleaningWorkspaceBO = (CleaningWorkspaceBO) workspaceBO;
            return deleteCleaningWorkspace(cleaningWorkspaceBO);
        } else {
            FusionWorkspaceBO fusionWorkspaceBO = (FusionWorkspaceBO) workspaceBO;
            return deleteFusionWorkspace(fusionWorkspaceBO);
        }
    }

    @Transactional
    public boolean deleteCleaningWorkspace(CleaningWorkspaceBO cleaningWorkspaceBO) {
        CleaningWorkspaceStatus cleaningWorkspaceStatus = cleaningWorkspaceBO.getStatus();
        int wsid = cleaningWorkspaceBO.getWsid();

        if (cleaningWorkspaceStatus == CleaningWorkspaceStatus.IMPORTING
                || cleaningWorkspaceStatus == CleaningWorkspaceStatus.ANALYZING
                || cleaningWorkspaceStatus == CleaningWorkspaceStatus.EXPORTING
                || cleaningWorkspaceStatus == CleaningWorkspaceStatus.TRANSFORMING) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_WORKING_DELETE);
        }

        List<WorkspaceEdition> workspaceEditions = cleaningWorkspaceDao.getWorkspaceEditions(wsid);
        for (WorkspaceEdition workspaceEdition : workspaceEditions) {
            //delete es & kafka topic
            int rsid = workspaceEdition.getRsid();
            RecordSource recordSource = recordSourceDao.getRecordSource(rsid);
            boolean indexDeleted = recordDao.deleteIndex(recordSource.getESIndexName());
            if (!indexDeleted) {
                logger.info("Failed to delete kafka topic data {}", new CleanerException(BizErrorEnum.WORKSPACE_ES_DELETE));
            }

            boolean topicDeleted = cleaningWorkspaceDao.deleteKakfa(recordSource.getKafkaTopic());
            if (!topicDeleted) {
                logger.info("Failed to delete kafka topic data {}", new CleanerException(BizErrorEnum.WORKSPACE_KAFKA_DELETE));
            }

            recordSourceDao.deleteRecordSource(recordSource.getRsid());
        }

        boolean workspaceEditionsDeleted = cleaningWorkspaceDao.deleteWorkspaceEditions(wsid);
        if (!workspaceEditionsDeleted) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_MYSQL_DELETE);
        }

        workspaceSourceDao.deleteByWsid(wsid);

        return workspaceDao.deleteCleaningWorkspace(wsid);
    }

    @Transactional
    public boolean deleteFusionWorkspace(FusionWorkspaceBO fusionWorkspaceBO) {
        int wsid = fusionWorkspaceBO.getWsid();
        workspaceSourceDao.deleteByWsid(wsid);
        workspaceDao.deleteFusionWorkspace(wsid);

        //delete Record Source
        RecordSource recordSource = fusionWorkspaceBO.getRecordSource();
        int rsid = recordSource.getRsid();
        List<DatamasterSourcePO> datamasterSources = datamasterSourceDao.getDatamasterSourcesByRsid(rsid);
        if (datamasterSources.isEmpty()) {
            recordSourceDao.deleteRecordSource(rsid);
            entropyService.deleteFusionStore(recordSource);
        }

        return true;
    }

}
