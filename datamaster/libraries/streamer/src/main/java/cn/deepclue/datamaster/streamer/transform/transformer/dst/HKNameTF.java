package cn.deepclue.datamaster.streamer.transform.transformer.dst;

import cn.deepclue.datamaster.streamer.transform.TFDef;

/**
 * Created by yuansheng on 6/19/17.
 */
@TFDef(semaName = "HK姓名清洗", summary = "列 ${sourceFieldName} 清洗HK姓名", order = 1)
public class HKNameTF extends DelCHSpaceTF {
    public HKNameTF(String sourceFieldName) {
        super(sourceFieldName);
    }
}
