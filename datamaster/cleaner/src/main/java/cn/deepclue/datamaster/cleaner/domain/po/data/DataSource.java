package cn.deepclue.datamaster.cleaner.domain.po.data;


import java.util.Date;

/**
 * Created by xuzb on 14/03/2017.
 * We can view it as a table.
 */
public class DataSource {
    private String dtname;
    private Integer dsid;
    private String dbname;
    private int dhid;
    private int rsid;
    private int status;
    private String description;
    private int ntotal;

    private Date createdOn;

    public DataSource() {
        //无参数的构造函数
    }

    public DataSource(int dhid, String dbname, String dtname) {
        this.dhid = dhid;
        this.dbname = dbname;
        this.dtname = dtname;
        this.createdOn = new Date();
    }

    public Integer getRsid() {
        return rsid;
    }

    public void setRsid(Integer rsid) {
        this.rsid = rsid;
    }

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public String getName() {
        return dtname;
    }

    public void setDtname(String dtname) {
        this.dtname = dtname;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public int getDhid() {
        return dhid;
    }

    public void setDhid(int dhid) {
        this.dhid = dhid;
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

    public String getDtname() {
        return dtname;
    }
}
