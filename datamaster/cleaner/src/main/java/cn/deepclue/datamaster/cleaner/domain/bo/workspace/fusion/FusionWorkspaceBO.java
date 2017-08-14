package cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.WorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.fusion.EntropyCalculationStatus;
import cn.deepclue.datamaster.cleaner.domain.fusion.FusionTaskStatus;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;

/**
 * Created by xuzb on 14/03/2017.
 */
public class FusionWorkspaceBO extends WorkspaceBO {
    //步骤
    private Integer step;
    //业务模型id
    private ObjectTypeBO objectTypeBO;
    //码值类型
    private Integer addressCodeType;
    //相似度阀值
    private Double threshold;
    //融合结果
    private RecordSource recordSource;
    //权值计算任务
    private Task entropyCalculationTask;
    //融合计算任务
    private Task similarityTask;

    public FusionTaskStatus getStatus() {
        if (getFinished() == FinishedStatus.FINISHED) {
            return FusionTaskStatus.FINISHED;
        }

        if (similarityTask == null) {
            return FusionTaskStatus.UNCALCULATED;
        }

        switch (similarityTask.getTaskStatus()) {
            case CANCLE:
                return FusionTaskStatus.UNCALCULATED;
            case PENDING:
            case RUNNING:
                return FusionTaskStatus.CALCULATING;
            case FINISHED:
                return FusionTaskStatus.CALCULATED;
            case FAILED:
                return FusionTaskStatus.FAILED;
        }

        return FusionTaskStatus.UNCALCULATED;
    }

    public EntropyCalculationStatus getEntropyCalculationStatus() {
        if (entropyCalculationTask == null) {
            return EntropyCalculationStatus.UNCALCULATED;
        }

        switch (entropyCalculationTask.getTaskStatus()) {
            case FAILED:
                return EntropyCalculationStatus.FAILED;
            case RUNNING:
                return EntropyCalculationStatus.CALCULATING;
            case FINISHED:
                return EntropyCalculationStatus.CALCULATED;
        }

        return EntropyCalculationStatus.UNCALCULATED;
    }

    public FusionStep getFusionStep() {
        return FusionStep.getFusionStep(step);
    }

    public void setFusionStep(FusionStep step) {
        this.step = step.getStep();
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public ObjectTypeBO getObjectTypeBO() {
        return objectTypeBO;
    }

    public void setObjectTypeBO(ObjectTypeBO objectTypeBO) {
        this.objectTypeBO = objectTypeBO;
    }

    public AddressCodeType getAddressCodeType() {
        return addressCodeType == null ? null : AddressCodeType.getAddressCodeType(addressCodeType);
    }

    public void setAddressCodeType(AddressCodeType addressCodeType) {
        this.addressCodeType = addressCodeType == null ? null : addressCodeType.getAddressCode();
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public RecordSource getRecordSource() {
        return recordSource;
    }

    public void setRecordSource(RecordSource recordSource) {
        this.recordSource = recordSource;
    }

    public Task getEntropyCalculationTask() {
        return entropyCalculationTask;
    }

    public void setEntropyCalculationTask(Task entropyCalculationTask) {
        this.entropyCalculationTask = entropyCalculationTask;
    }

    public Task getSimilarityTask() {
        return similarityTask;
    }

    public void setSimilarityTask(Task similarityTask) {
        this.similarityTask = similarityTask;
    }
}
