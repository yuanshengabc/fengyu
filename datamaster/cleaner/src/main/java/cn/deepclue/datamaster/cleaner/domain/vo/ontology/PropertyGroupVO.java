package cn.deepclue.datamaster.cleaner.domain.vo.ontology;

/**
 * Created by magneto on 17-4-9.
 */
public class PropertyGroupVO{
    private Integer pgid;
    private Integer otid;
    private String name;
    private Integer ptCount;


    public Integer getPgid() {
        return pgid;
    }

    public void setPgid(Integer pgid) {
        this.pgid = pgid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPtCount() {
        return ptCount;
    }

    public void setPtCount(Integer ptCount) {
        this.ptCount = ptCount;
    }

    public Integer getOtid() {
        return otid;
    }

    public void setOtid(Integer otid) {
        this.otid = otid;
    }
}
