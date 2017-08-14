package cn.deepclue.datamaster.cleaner.domain.bo.data;

import java.util.Date;

/**
 * 外部数据源BO
 * Created by sunxingwen on 17-5-18.
 */
public class DataSourceBO {
    private String dtname;
    private Integer dsid;
    private String dbname;
    private int rsid;
    private int status;
    private String description;
    private int ntotal;

    private Date createdOn;

    private DataHouse dataHouse;

    public String getDtname() {
        return dtname;
    }

    public void setDtname(String dtname) {
        this.dtname = dtname;
    }

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public int getRsid() {
        return rsid;
    }

    public void setRsid(int rsid) {
        this.rsid = rsid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNtotal() {
        return ntotal;
    }

    public void setNtotal(int ntotal) {
        this.ntotal = ntotal;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public DataHouse getDataHouse() {
        return dataHouse;
    }

    public void setDataHouse(DataHouse dataHouse) {
        this.dataHouse = dataHouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSourceBO that = (DataSourceBO) o;

        return getDsid().equals(that.getDsid());

    }

    @Override
    public int hashCode() {
        return getDsid().hashCode();
    }
}
