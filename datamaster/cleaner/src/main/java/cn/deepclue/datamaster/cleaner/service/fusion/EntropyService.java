package cn.deepclue.datamaster.cleaner.service.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.EntropyPropertyPO;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;

import java.util.List;

/**
 * Created by magneto on 17-5-23.
 */
public interface EntropyService {
    EntropyTableBO getEntropyTable(int fwsid);

    EntropyTableBO getSelectedEntropyTable(int fwsid);

    boolean deleteEntroipes(RecordSource recordSource);

    Integer deleteEntropyTable(int fwsid);

    boolean insertEntropyTable(List<EntropyPropertyPO> entropyPropertyPOs);

    PreviewStats getFusionPreviewInfo(RecordSource recordSource);

    Long getSingleFieldFusionNum(RecordSource recordSource);

    Long getMultiFieldFusionNum(RecordSource recordSource);

    boolean deleteFusionStore(RecordSource recordSource);
}
