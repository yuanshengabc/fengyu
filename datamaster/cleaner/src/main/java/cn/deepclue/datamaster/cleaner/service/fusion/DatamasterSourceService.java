package cn.deepclue.datamaster.cleaner.service.fusion;

import cn.deepclue.datamaster.cleaner.domain.vo.fusion.DatamasterSourceListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.FusionSourcesVO;

public interface DatamasterSourceService {
    DatamasterSourceListVO getDatamasterSources(int page, int pageSize);

    boolean deleteDatamasterSource(int dsid);

    FusionSourcesVO getFusionSources();
}
