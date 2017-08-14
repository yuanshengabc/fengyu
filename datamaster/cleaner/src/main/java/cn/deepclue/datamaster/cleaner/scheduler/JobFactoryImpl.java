package cn.deepclue.datamaster.cleaner.scheduler;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DataHouseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DataSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.*;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionSourceService;
import cn.deepclue.datamaster.cleaning.job.*;
import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.job.EntropyComputeJob;
import cn.deepclue.datamaster.fusion.job.SimilarityComputeJob;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.streamer.config.*;
import cn.deepclue.datamaster.streamer.job.Job;
import cn.deepclue.datamaster.streamer.transform.Rewriter;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.Transformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 08/04/2017.
 */
@Component("jobFactory")
public class JobFactoryImpl implements JobFactory {

    @Autowired
    private CleanerConfigurationProperties properties;

    @Autowired
    private DataSourceDao dataSourceDao;

    @Autowired
    private DataHouseDao dataHouseDao;

    @Autowired
    private RecordSourceDao recordSourceDao;

    @Autowired
    private FusionWorkspaceDao fusionWorkspaceDao;

    @Autowired
    private FusionSourceService fusionSourceService;

    @Autowired
    private EntropyService entropyService;

    public Job createJob(Task task) {
        switch (task.getTaskType()) {
            case IMPORT:
                return createImportJob(task);
            case ANALYSIS:
                return createAnalysisJob(task);
            case TRANSFORM:
                return createTransformJob(task);
            case EXPORT:
                return createExportJob(task);
            case ENTROPY_COMPUTE:
                return createEntropyComputeJob(task);
            case SIMILARITY_COMPUTE:
                return createSimilarityComputeJob(task);
            case PERSISTENCE:
                return createPersistenceJob(task);

            default:
                throw new IllegalStateException("Unknown task type: " + task.getTaskType());
        }
    }

    private FusionConfig createFusionConfig(RecordSource recordSource) {
        FusionConfig fusionConfig = new FusionConfig();
        fusionConfig.setFusionKey(recordSource.getFusionKey());
        fusionConfig.setHdfsConfig(properties.getHdfsConfig());

        return fusionConfig;
    }

    private Job createSimilarityComputeJob(Task task) {
        int wsid = Integer.parseInt(task.getExtraData());
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(wsid);
        FusionConfig fusionConfig = createFusionConfig(fusionWorkspaceBO.getRecordSource());

        // Create mysql sources from fusion data source.
        List<MySQLTableConfig> mysqlSources = new ArrayList<>();
        List<FusionDataSourceBO> fusionDataSourceBOs = fusionSourceService.getFusionDataSourceBOs(wsid);
        for (FusionDataSourceBO fusionDataSourceBO : fusionDataSourceBOs) {
            MySQLTableConfig mysqlSource = createMySQLTableConfig(fusionDataSourceBO.getDataSourceBO());
            mysqlSources.add(mysqlSource);
        }

        // Add fusion sources
        List<FusionConfig> fusionSources = new ArrayList<>();
        List<FusionDatamasterSourceBO> fusionDatamasterSourceBOs = fusionSourceService.getFusionDatamasterSourceBOs(wsid);
        for (FusionDatamasterSourceBO fusionDatamasterSourceBO : fusionDatamasterSourceBOs) {
            RecordSource recordSource = recordSourceDao.getRecordSource(fusionDatamasterSourceBO.getRsid());
            FusionConfig fusionSource = createFusionConfig(recordSource);
            fusionSources.add(fusionSource);
        }

        // Get selected field entropies.
        List<FieldEntropy> fieldEntropies = new ArrayList<>();
        EntropyTableBO entropyTableBO = entropyService.getSelectedEntropyTable(wsid);
        for (EntropyFieldBO entropyFieldBO : entropyTableBO.getEntropyFields()) {
            FieldEntropy entropy = new FieldEntropy();
            entropy.setEntropy(entropyFieldBO.getEntropy());
            entropy.setFieldName(entropyFieldBO.getPropertyType().getName());
            entropy.setUniqued(entropyFieldBO.getUnique());

            fieldEntropies.add(entropy);
        }

        Double threshold = fusionWorkspaceBO.getThreshold();

        return new SimilarityComputeJob(task.getTid(), mysqlSources, fusionSources, fusionConfig, fieldEntropies, threshold);
    }

    private Job createEntropyComputeJob(Task task) {
        int fwsid = Integer.parseInt(task.getExtraData());
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        FusionConfig fusionConfig = createFusionConfig(fusionWorkspaceBO.getRecordSource());

        // Create mysql sources from fusion data source.
        List<MySQLTableConfig> mysqlSources = new ArrayList<>();
        List<FusionDataSourceBO> fusionDataSourceBOs = fusionSourceService.getFusionDataSourceBOs(fwsid);
        for (FusionDataSourceBO fusionDataSourceBO : fusionDataSourceBOs) {
            MySQLTableConfig mysqlSource = createMySQLTableConfig(fusionDataSourceBO.getDataSourceBO());
            mysqlSources.add(mysqlSource);
        }

        // Build fusion sources
        List<FusionConfig> fusionSources = new ArrayList<>();
        List<FusionDatamasterSourceBO> fusionDatamasterSourceBOs = fusionSourceService.getFusionDatamasterSourceBOs(fwsid);
        for (FusionDatamasterSourceBO fusionDatamasterSourceBO : fusionDatamasterSourceBOs) {
            int rsid = fusionDatamasterSourceBO.getRsid();
            RecordSource recordSource = recordSourceDao.getRecordSource(rsid);
            FusionConfig fusionSource = createFusionConfig(recordSource);
            fusionSources.add(fusionSource);
        }

        ObjectTypeBO objectTypeBO = fusionWorkspaceBO.getObjectTypeBO();
        List<PropertyType> propertyTypes = objectTypeBO.getPropertyTypes();

        return new EntropyComputeJob(task.getTid(), fusionConfig, propertyTypes, fusionSources, mysqlSources);
    }

    private MySQLTableConfig createMySQLTableConfig(DataSourceBO dataSource) {
        DataHouse dataHouse = dataSource.getDataHouse();
        if (dataHouse == null) {
            throw new CleanerException(BizErrorEnum.DATAHOUSE_DELETED);
        }

        MySQLConfig mysqlConfig = new MySQLConfig();
        mysqlConfig.setDatabase(dataSource.getDbname());
        mysqlConfig.setIp(dataHouse.getIp());
        mysqlConfig.setPassword(dataHouse.getPassword());
        mysqlConfig.setPort(dataHouse.getPort());
        mysqlConfig.setUsername(dataHouse.getUsername());

        MySQLTableConfig mysqlTableConfig = new MySQLTableConfig();
        mysqlTableConfig.setMysqlConfig(mysqlConfig);
        mysqlTableConfig.setTableName(dataSource.getDtname());

        return mysqlTableConfig;
    }

    private Job createImportJob(Task task) {
        // Create mysql config from data source
        int dsid = task.getSourceId();
        DataSourceBO dataSource = dataSourceDao.getDataSourceBO(dsid);

        MySQLTableConfig mySQLSource = createMySQLTableConfig(dataSource);

        // Create kafka config from record source
        int rsid = task.getSinkId();
        RecordSource recordSource = recordSourceDao.getRecordSource(rsid);

        KTopicConfig kafkaSink = new KTopicConfig();
        kafkaSink.setTopic(recordSource.getKafkaTopic());
        kafkaSink.setKconfig(properties.getKafkaConfig());

        return new ImportMySQLJob(task.getTid(), mySQLSource, kafkaSink);
    }

    private Job createAnalysisJob(Task task) {
        // Create kafka topic config from record source
        int rsid = task.getSourceId();
        RecordSource recordSource = recordSourceDao.getRecordSource(rsid);

        KTopicConfig kafkaSource = new KTopicConfig();
        kafkaSource.setKconfig(properties.getKafkaConfig());
        kafkaSource.setTopic(recordSource.getKafkaTopic());

        ESTypeConfig esSink = new ESTypeConfig(properties.getEsconfig(), recordSource.getESIndexName(), recordSource.getESTypeName());

        return new AnalysisJob(task.getTid(), kafkaSource, esSink);
    }

    private Job createTransformJob(Task task) {
        int sourceRsid = task.getSourceId();
        RecordSource rssource = recordSourceDao.getRecordSource(sourceRsid);
        KTopicConfig kafkaSource = new KTopicConfig();
        kafkaSource.setKconfig(properties.getKafkaConfig());
        kafkaSource.setTopic(rssource.getKafkaTopic());

        int sinkRsid = task.getSinkId();
        RecordSource rssink = recordSourceDao.getRecordSource(sinkRsid);
        KTopicConfig kafkaSink = new KTopicConfig();
        kafkaSink.setKconfig(properties.getKafkaConfig());
        kafkaSink.setTopic(rssink.getKafkaTopic());

        String extraData = task.getExtraData();
        List<Transformation> transformations = TransformHelper.deserializeTransformations(extraData);

        return new TransformJob(task.getTid(), kafkaSource, kafkaSink, transformations);
    }

    private Job createExportJob(Task task) {
        int rsid = task.getSourceId();
        RecordSource rssource = recordSourceDao.getRecordSource(rsid);

        KTopicConfig kafkaSource = new KTopicConfig();
        kafkaSource.setKconfig(properties.getKafkaConfig());
        kafkaSource.setTopic(rssource.getKafkaTopic());

        int dsid = task.getSinkId();
        DataSourceBO dataSource = dataSourceDao.getDataSourceBO(dsid);

        MySQLTableConfig mySQLSink = createMySQLTableConfig(dataSource);

        String extraData = task.getExtraData();
        Rewriter rewriter = TransformHelper.deserializeRewriters(extraData);

        return new StartOverExport2MySQLJob(task.getTid(), kafkaSource, mySQLSink, rewriter);
    }

    private Job createPersistenceJob(Task task) {
        int sourceRsid = task.getSourceId();
        RecordSource rssource = recordSourceDao.getRecordSource(sourceRsid);

        KTopicConfig kafkaSource = new KTopicConfig();
        kafkaSource.setKconfig(properties.getKafkaConfig());
        kafkaSource.setTopic(rssource.getKafkaTopic());

        int sinkRsid = task.getSinkId();
        RecordSource rssink = recordSourceDao.getRecordSource(sinkRsid);

        HDFSFileConfig hdfsFileConfig = new HDFSFileConfig();
        hdfsFileConfig.setHdfsConfig(properties.getHdfsConfig());
        hdfsFileConfig.setFilePath(rssink.getHDFSFilePath());

        return new Persistence2HDFSJob(task.getTid(), kafkaSource, hdfsFileConfig);
    }
}
