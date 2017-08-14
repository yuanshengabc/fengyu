package cn.deepclue.datamaster.cleaner.service.fusion.impl;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.DatamasterSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.DatamasterSourceListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.FusionSourcesVO;
import cn.deepclue.datamaster.cleaner.service.fusion.DatamasterSourceService;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("datamasterSourceService")
public class DatamasterSourceServiceImpl implements DatamasterSourceService {
    @Autowired
    private DatamasterSourceDao datamasterSourceDao;
    @Autowired
    private FusionWorkspaceDao fusionWorkspaceDao;
    @Autowired
    private RecordSourceDao recordSourceDao;
    @Autowired
    private EntropyService entropyService;
    @Autowired
    private CleanerConfigurationProperties properties;

    @Override
    public DatamasterSourceListVO getDatamasterSources(int page, int pageSize) {
        DatamasterSourceListVO datamasterSourceList = new DatamasterSourceListVO();
        datamasterSourceList.setDsCount(datamasterSourceDao.getDatamasterSourceCount());
        datamasterSourceList.setDatamasterSources(datamasterSourceDao.getDatamasterSources(page, pageSize));
        return datamasterSourceList;
    }

    @Override
    public boolean deleteDatamasterSource(int dsid) {
        DatamasterSourcePO datamasterSource = datamasterSourceDao.getDatamasterSourceByDsid(dsid);

        datamasterSourceDao.deleteDatamasterSources(dsid);

        //delete Record Source
        int rsid = datamasterSource.getRsid();
        FusionWorkspacePO fusionWorkspace = fusionWorkspaceDao.getFusionWorkspaceByRsid(rsid);
        List<DatamasterSourcePO> datamasterSources = datamasterSourceDao.getDatamasterSourcesByRsid(rsid);
        if (fusionWorkspace == null && datamasterSources.isEmpty()) {
            RecordSource recordSource = recordSourceDao.getRecordSource(rsid);
            recordSourceDao.deleteRecordSource(rsid);
            entropyService.deleteFusionStore(recordSource);
        }

        return true;
    }

    @Override
    public FusionSourcesVO getFusionSources() {
        FusionSourcesVO fusionSourcesVO = new FusionSourcesVO();

        HDFSConfig hdfsConfig = properties.getHdfsConfig();
        String hdfs = String.format("hdfs://%s:%d", hdfsConfig.getServer(), hdfsConfig.getPort());
        fusionSourcesVO.setHdfs(hdfs);

        List<DatamasterSourcePO> datamasterSourcePOs = datamasterSourceDao.getDatamasterSources();
        List<FusionSourcesVO.FusionSourceVO> fusionSources = datamasterSourcePOs.stream().map(
                po -> {
                    FusionSourcesVO.FusionSourceVO fsVo = new FusionSourcesVO.FusionSourceVO();
                    fsVo.setName(po.getName());
                    fsVo.setDescription(po.getDescription());
                    fsVo.setDmsid(po.getDsid());

                    Integer rsid = po.getRsid();
                    RecordSource recordSource = recordSourceDao.getRecordSource(rsid);
                    fsVo.setFusionKey(recordSource.getFusionKey());
                    return fsVo;
                }
        ).collect(toList());
        fusionSourcesVO.setFusionSources(fusionSources);

        return fusionSourcesVO;
    }
}
