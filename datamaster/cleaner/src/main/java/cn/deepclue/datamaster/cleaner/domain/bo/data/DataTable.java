package cn.deepclue.datamaster.cleaner.domain.bo.data;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by magneto on 17-3-28.
 */
public class DataTable {
    @NotNull
    private Integer dhid;

    @NotBlank
    private String dbname;

    @NotBlank
    private String dtname;

    private int ntotal;

    private String description;

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

    public int getNtotal() {
        return ntotal;
    }

    public void setNtotal(int ntotal) {
        this.ntotal = ntotal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
