package cn.deepclue.datamaster.cleaner.domain.po.cleaning;

/**
 * Created by xuzb on 07/04/2017.
 */
public class OntologyMappingPO {
    private Integer omid;
    private Integer wsid;
    private Integer wsversion;
    private String fieldName;
    private Integer ptid;

    public Integer getOmid() {
        return omid;
    }

    public void setOmid(Integer omid) {
        this.omid = omid;
    }

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

