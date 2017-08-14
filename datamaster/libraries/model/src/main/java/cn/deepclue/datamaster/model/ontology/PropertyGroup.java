package cn.deepclue.datamaster.model.ontology;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by magneto on 17-3-31.
 * 不属于任何属性组的属性，其pgid为0
 */
public class PropertyGroup {
    private Integer pgid;
    @NotNull
    private Integer otid;
    @NotBlank(message = "name can not be null")
    private String name;

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

    public Integer getOtid() {
        return otid;
    }

    public void setOtid(Integer otid) {
        this.otid = otid;
    }
}
