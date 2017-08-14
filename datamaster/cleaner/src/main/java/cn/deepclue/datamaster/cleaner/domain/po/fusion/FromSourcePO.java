package cn.deepclue.datamaster.cleaner.domain.po.fusion;

/**
 * Created by magneto on 17-6-6.
 */
public class FromSourcePO {
    private Integer fdsid;
    private Integer dsid;
    private Integer from;
    private Integer stype;

    public Integer getFdsid() {
        return fdsid;
    }

    public void setFdsid(Integer fdsid) {
        this.fdsid = fdsid;
    }

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getStype() {
        return stype;
    }

    public void setStype(Integer stype) {
        this.stype = stype;
    }
}
