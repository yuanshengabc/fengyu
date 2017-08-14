package cn.deepclue.datamaster.cleaner.domain.po.fusion;

/**
 * Created by magneto on 17-5-23.
 */
public class EntropyPropertyPO {
    private Integer wsptid;
    //工作空间id
    private Integer wsid;
    //属性id
    private Integer ptid;
    //是否唯一列
    private Boolean unique;

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public Integer getPtid() {
        return ptid;
    }

    public void setPtid(Integer ptid) {
        this.ptid = ptid;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Integer getWsptid() {
        return wsptid;
    }

    public void setWsptid(Integer wsptid) {
        this.wsptid = wsptid;
    }
}
