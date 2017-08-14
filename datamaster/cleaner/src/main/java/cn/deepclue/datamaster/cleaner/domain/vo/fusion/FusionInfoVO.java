package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.fusion.EntropyCalculationStatus;

/**
 * 设置融合方式页面状态信息
 * Created by magneto on 17-5-17.
 */
public class FusionInfoVO {
    //码址类型
    private AddressCodeType addressCodeType;
    //权值计算状态
    private EntropyCalculationStatus entropyCalculationStatus;
    //相似度阀值
    private Double threshold;

    public AddressCodeType getAddressCodeType() {
        return addressCodeType == null ? AddressCodeType.SINGLE_ADDRESS_CODE : addressCodeType;
    }

    public void setAddressCodeType(AddressCodeType addressCodeType) {
        this.addressCodeType = addressCodeType;
    }

    public EntropyCalculationStatus getEntropyCalculationStatus() {
        return entropyCalculationStatus;
    }

    public void setEntropyCalculationStatus(EntropyCalculationStatus entropyCalculationStatus) {
        this.entropyCalculationStatus = entropyCalculationStatus;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public FusionInfoVO from(FusionWorkspaceBO fusionWorkspaceBO) {
        this.setAddressCodeType(fusionWorkspaceBO.getAddressCodeType());
        this.setEntropyCalculationStatus(fusionWorkspaceBO.getEntropyCalculationStatus());
        this.setThreshold(fusionWorkspaceBO.getThreshold());
        return this;
    }
}
