package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next;

import cn.deepclue.datamaster.cleaner.dao.cleaning.TaskDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyFieldBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by magneto on 17-5-22.
 */
@Component
@Scope("prototype")
public class SetFusionNext extends FusionNextStep {
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private FusionWorkspaceDao fusionWorkspaceDao;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private EntropyService entropyService;

    public SetFusionNext(FusionWorkspaceBO fusionWorkspaceBO) {
        super(fusionWorkspaceBO);
        setStep(FusionStep.FUSION_MODE.getStep());
    }

    @Override
    protected boolean valid() {
        AddressCodeType addressCodeType = fusionWorkspaceBO.getAddressCodeType();
        EntropyTableBO entropyTableBO = entropyService.getSelectedEntropyTable(fusionWorkspaceBO.getWsid());
        List<EntropyFieldBO> entropyFieldBOs = entropyTableBO.getEntropyFields();

        if (addressCodeType == null || entropyFieldBOs == null) {
            throw new CleanerException("AddressCodeType is empty or entropy fields is empty!", "码址类型为空或列表为空！", BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
        }

        if (addressCodeType == AddressCodeType.SINGLE_ADDRESS_CODE && entropyFieldBOs.size() != 1) {
            throw new CleanerException("Please select only one column of data!", "请选择一列数据！", BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
        }

        if (addressCodeType == AddressCodeType.MULTI_ADDRESS_CODE) {
            if (entropyFieldBOs.size() <= 1) {
                throw new CleanerException("Please select multiple column of data!", "请选择多列数据！", BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
            }

            double threshold = fusionWorkspaceBO.getThreshold();
            double similarity = entropyTableBO.similarity();
            if (threshold > similarity || threshold < 0) {
                throw new CleanerException("Unreasonable score!!", "不合理的分值！", BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
            }
        }

        return true;
    }

    @Override
    protected void work() {
        super.work();
        Task task = taskDao.createSimilarityComputeTask(fusionWorkspaceBO);
        fusionWorkspaceDao.updateSimilarityTask(fusionWorkspaceBO.getWsid(), task.getTid());
        taskScheduler.schedule(task);
    }
}
