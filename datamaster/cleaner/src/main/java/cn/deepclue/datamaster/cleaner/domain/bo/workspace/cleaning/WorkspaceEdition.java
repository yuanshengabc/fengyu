package cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.WorkspaceEditionPO;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by xuzb on 17/03/2017.
 */
public class WorkspaceEdition {
    // Make weid as primary key
    private int weid;
    private int wsid;
    private int wsversion;

    // The record source id
    private int rsid;

    // object type id
    private ObjectType objectType;


    private Date modifiedOn;

    private Task importTask;

    private Task analysisTask;

    private Task transformTask;

    private Task exportTask;

    public WorkspaceEditionStatus getStatus() {
        if (transformTask == null) {
            return WorkspaceEditionStatus.UNCLEANED;
        }

        if (exportTask == null) {
            return WorkspaceEditionStatus.UNEXPORTED;
        }

        switch (exportTask.getTaskStatus()) {
            case FAILED:
            case CANCLE:
                return WorkspaceEditionStatus.UNEXPORTED;
            case FINISHED:
                return WorkspaceEditionStatus.EXPORTED;
            case RUNNING:
            case PENDING:
                return WorkspaceEditionStatus.EXPORTING;

        }

        return WorkspaceEditionStatus.UNCLEANED;
    }

    public static WorkspaceEditionPO toPO(WorkspaceEdition workspaceEdition) {
        WorkspaceEditionPO workspaceEditionPO = new WorkspaceEditionPO();
        BeanUtils.copyProperties(workspaceEdition, workspaceEditionPO);
        ObjectType objectType = workspaceEdition.getObjectType();
        if (objectType != null) {
            workspaceEditionPO.setOtid(objectType.getOtid());
        }

        return workspaceEditionPO;
    }

    public int getWeid() {
        return weid;
    }

    public void setWeid(int weid) {
        this.weid = weid;
    }

    public int getWsid() {
        return wsid;
    }

    public void setWsid(int wsid) {
        this.wsid = wsid;
    }

    public int getRsid() {
        return rsid;
    }

    public void setRsid(int rsid) {
        this.rsid = rsid;
    }

    public int getWsversion() {
        return wsversion;
    }

    public void setWsversion(int wsversion) {
        this.wsversion = wsversion;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Task getImportTask() {
        return importTask;
    }

    public void setImportTask(Task importTask) {
        this.importTask = importTask;
    }

    public Task getExportTask() {
        return exportTask;
    }

    public void setExportTask(Task exportTask) {
        this.exportTask = exportTask;
    }

    public Task getAnalysisTask() {
        return analysisTask;
    }

    public void setAnalysisTask(Task analysisTask) {
        this.analysisTask = analysisTask;
    }

    public Task getTransformTask() {
        return transformTask;
    }

    public void setTransformTask(Task transformTask) {
        this.transformTask = transformTask;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
