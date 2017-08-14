package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyFieldBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.fusion.FusionTaskStatus;
import cn.deepclue.datamaster.fusion.domain.IntervalNumber;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 开始融合页面状态信息VO
 * Created by sunxingwen on 17-5-18.
 */
public class FusionTaskStatusInfoVO {
    //码址类型
    private AddressCodeType addressCodeType;
    //状态
    private FusionTaskStatus fusionTaskStatus;
    //状态开始时间
    private Date startTime;
    //相似度
    private Double similarity;
    //单码址融合数据量
    private Long dsCount;
    //多码址融合数据预览
    private List<IntervalNumberVO> intervalNumbers;

    public AddressCodeType getAddressCodeType() {
        return addressCodeType == null ? AddressCodeType.SINGLE_ADDRESS_CODE : addressCodeType;
    }

    public void setAddressCodeType(AddressCodeType addressCodeType) {
        this.addressCodeType = addressCodeType;
    }

    public FusionTaskStatus getFusionTaskStatus() {
        return fusionTaskStatus;
    }

    public void setFusionTaskStatus(FusionTaskStatus fusionTaskStatus) {
        this.fusionTaskStatus = fusionTaskStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Long getDsCount() {
        return dsCount;
    }

    public void setDsCount(Long dsCount) {
        this.dsCount = dsCount;
    }

    public List<IntervalNumberVO> getIntervalNumbers() {
        return intervalNumbers;
    }

    public void setIntervalNumbers(List<IntervalNumberVO> intervalNumbers) {
        this.intervalNumbers = intervalNumbers;
    }

    public FusionTaskStatusInfoVO from(FusionWorkspaceBO fusionWorkspaceBO, EntropyTableBO entropyTableBO,
                                       PreviewStats previewStats, Long fieldFusionNum) {
        this.setAddressCodeType(fusionWorkspaceBO.getAddressCodeType());
        this.setFusionTaskStatus(fusionWorkspaceBO.getStatus());
        if (fusionWorkspaceBO.getEntropyCalculationTask() == null) {
            this.setStartTime(null);
        } else {
            if (fusionWorkspaceBO.getSimilarityTask() == null) {
                this.setStartTime(fusionWorkspaceBO.getEntropyCalculationTask().getModifiedOn());
            } else {
                this.setStartTime(fusionWorkspaceBO.getSimilarityTask().getModifiedOn());
            }
        }
        if (entropyTableBO != null) {
            BigDecimal total = calculateTotal(entropyTableBO);
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal tempSimilarity = new BigDecimal(fusionWorkspaceBO.getThreshold().toString())
                        .divide(total, 10, BigDecimal.ROUND_HALF_UP);
                this.setSimilarity(tempSimilarity.doubleValue());

                if (previewStats != null) {
                    List<IntervalNumberVO> tempIntervalNumbers = new LinkedList<>();
                    for (IntervalNumber intervalNumber : previewStats.getIntervalNumbers()) {
                        tempIntervalNumbers.add(new IntervalNumberVO().from(intervalNumber, total));
                    }
                    this.setIntervalNumbers(tempIntervalNumbers);
                }
            }
        }

        this.setDsCount(fieldFusionNum);
        return this;
    }

    private BigDecimal calculateTotal(EntropyTableBO entropyTableBO) {
        List<EntropyFieldBO> entropyFieldBOs = entropyTableBO.getEntropyFields();
        BigDecimal total = BigDecimal.ZERO;
        for (EntropyFieldBO entropyFieldBO : entropyFieldBOs) {
            if (entropyFieldBO.getSelected() && !entropyFieldBO.getUnique()) {
                total = total.add(new BigDecimal(entropyFieldBO.getEntropy().toString()));
            }
        }
        return total;
    }
}
