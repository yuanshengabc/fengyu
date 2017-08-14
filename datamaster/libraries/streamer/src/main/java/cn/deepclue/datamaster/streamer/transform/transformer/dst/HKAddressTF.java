package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by yuansheng on 6/19/17.
 */
@TFDef(semaName = "HK地址清洗", summary = "列 ${sourceFieldName} 清洗HK地址", order = 1)
public class HKAddressTF extends DelCHSpaceTF {
    public HKAddressTF(String sourceFieldName) {
        super(sourceFieldName);
    }
}
