package cn.deepclue.datamaster.cleaner.domain.po.cleaning;

/**
 * Created by xuzb on 18/04/2017.
 */
public class TransformationPO {
    private Integer tfid;
    private String tftype;
    private Integer wsid;
    private Integer wsversion;
    private String args;
    private String filters;

    public Integer getTfid() {
        return tfid;
    }

    public void setTfid(Integer tfid) {
        this.tfid = tfid;
    }

    public String getTftype() {
        return tftype;
    }

    public void setTftype(String tftype) {
        this.tftype = tftype;
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

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}
