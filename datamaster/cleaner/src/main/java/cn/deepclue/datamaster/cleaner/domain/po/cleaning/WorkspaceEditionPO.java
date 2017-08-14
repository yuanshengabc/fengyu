package cn.deepclue.datamaster.cleaner.domain.po.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEditionStatus;

import java.util.Date;

/**
 * Created by xuzb on 17/03/2017.
 */
public class WorkspaceEditionPO {
    // Make wsid and version as primary key
    private Integer wsid;
    private Integer wsversion;

    // The record source id
    private Integer rsid;

    // object type id
    private Integer otid;

    private Date modifiedOn;

    private Task importTask;

    private Task analysisTask;

    private Task transformTask;

    private Task exportTask;

    private Integer status;

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public Integer getWsversion() {
        return wsversion;
    }

    public void setWsversion(Integer wsversion) {
        this.wsversion = wsversion;
    }

    public Integer getRsid() {
        return rsid;
    }

    public void setRsid(Integer rsid) {
        this.rsid = rsid;
    }

    public Integer getOtid() {
        return otid;
    }

    public void setOtid(Integer otid) {
        this.otid = otid;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public void setWorkspaceStatus(WorkspaceEditionStatus workspaceEditionStatus) {
        this.status = workspaceEditionStatus.getValue();
    }

    public Integer getStatus() {
        return status;
    }

    public Task getImportTask() {
        return importTask;
    }

    public void setImportTask(Task importTask) {
        this.importTask = importTask;
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

    public Task getExportTask() {
        return exportTask;
    }

    public void setExportTask(Task exportTask) {
        this.exportTask = exportTask;
    }
}
