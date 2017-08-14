package cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.FusionTaskStatus;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceVO;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by magneto on 17-5-15.
 */
public class FusionWorkspaceVO extends WorkspaceVO {
    private FusionTaskStatus status;
    private FusionStep step;
    private ObjectType objectType;
    private List<DataSource> dataSources;
    private RecordSource recordSource;

    public WorkspaceVO fromBO(FusionWorkspaceBO fusionWorkspaceBO) {
        BeanUtils.copyProperties(fusionWorkspaceBO, this);
        ObjectTypeBO objectTypeBO = fusionWorkspaceBO.getObjectTypeBO();
        if (objectTypeBO != null) {
            objectType = new ObjectType();
            BeanUtils.copyProperties(objectTypeBO, objectType);
        }
        this.step = fusionWorkspaceBO.getFusionStep();
        return this;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public FusionTaskStatus getStatus() {
        return status;
    }

    public void setStatus(FusionTaskStatus status) {
        this.status = status;
    }

    public FusionStep getStep() {
        return step;
    }

    public void setStep(FusionStep step) {
        this.step = step;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public RecordSource getRecordSource() {
        return recordSource;
    }

    public void setRecordSource(RecordSource recordSource) {
        this.recordSource = recordSource;
    }
}
