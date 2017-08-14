package cn.deepclue.datamaster.cleaner.domain.vo.ontology;

/**
 * Created by magneto on 17-4-10.
 */
public class ObjectTypeVO {
    private Integer otid;
    private String name;
    private String description;

    public Integer getOtid() {
        return otid;
    }

    public void setOtid(Integer otid) {
        this.otid = otid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
