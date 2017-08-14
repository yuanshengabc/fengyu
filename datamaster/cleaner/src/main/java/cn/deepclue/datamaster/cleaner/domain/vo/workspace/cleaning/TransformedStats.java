package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordStatsRespVO;

/**
 * Created by xuzb on 20/06/2017.
 */
public class TransformedStats {
    private boolean supported;
    private RecordStatsRespVO stats;

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    public RecordStatsRespVO getStats() {
        return stats;
    }

    public void setStats(RecordStatsRespVO stats) {
        this.stats = stats;
    }
}
