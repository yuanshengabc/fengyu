package cn.deepclue.datamaster.cleaner.dao.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;

/**
 * Created by magneto on 17-5-17.
 */
public interface FusionWorkspaceDao {
    FusionWorkspaceBO getWorkspace(int fswid);

    FusionWorkspacePO getFusionWorkspaceByRsid(int rsid);

    boolean updateObjectType(int fwsid, Integer otid);

    boolean updateAddressCodeType(int fwsid, AddressCodeType addressCodeType);

    boolean updateStep(int fwsid, int step);

    boolean updateThreshold(int fwsid, Double threshold);

    boolean updateSimilarityTask(int fwsid, Integer tid);

    boolean updateEntropyCalculationTask(int fwsid, Integer tid);
}
