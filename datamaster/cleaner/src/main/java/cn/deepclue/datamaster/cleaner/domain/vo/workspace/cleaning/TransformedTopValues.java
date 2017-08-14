package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import cn.deepclue.datamaster.cleaner.domain.vo.data.TopValueVO;

import java.util.List;

/**
 * Created by xuzb on 10/05/2017.
 */
public class TransformedTopValues {
    boolean supported;
    List<TopValueVO> topValues;

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    public List<TopValueVO> getTopValues() {
        return topValues;
    }

    public void setTopValues(List<TopValueVO> topValues) {
        this.topValues = topValues;
    }
}
