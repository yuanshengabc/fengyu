package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import javax.validation.constraints.NotNull;

/**
 * Created by xuzb on 08/04/2017.
 */
public class AddOntologyMappingReqVO {
    @NotNull
    private Integer wsid;
    @NotNull
    private Integer wsversion;
    @NotNull
    private String fieldName;

    private Integer ptid;

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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getPtid() {
        return ptid;
    }

    public void setPtid(Integer ptid) {
        this.ptid = ptid;
    }
}
