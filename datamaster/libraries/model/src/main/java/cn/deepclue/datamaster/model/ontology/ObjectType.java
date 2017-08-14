package cn.deepclue.datamaster.model.ontology;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by magneto on 17-3-31.
 */
public class ObjectType {
    private Integer otid;
    @NotBlank(message = "name can not be blank")
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
