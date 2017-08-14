package cn.deepclue.datamaster.cleaner.domain.vo.workspace;

import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by xuzb on 08/04/2017.
 */
public class CreateWorkspaceReqVO {
    private Integer uid;

    private String wstype;

    @NotBlank
    private String name;

    private String description;

//    @NotNull
    private Integer dhid;

//    @NotBlank
    private String dbname;

//    @NotBlank
    private String dtname;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public Integer getDhid() {
        return dhid;
    }

    public void setDhid(Integer dhid) {
        this.dhid = dhid;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDtname() {
        return dtname;
    }

    public void setDtname(String dtname) {
        this.dtname = dtname;
    }

    public WorkspaceType getWstype() {
        return WorkspaceType.valueOf(wstype);
    }

    public void setWstype(WorkspaceType wstype) {
        this.wstype = wstype.name();
    }
}
