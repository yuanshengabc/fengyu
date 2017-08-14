package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

/**
 * 外部数据源
 * Created by magneto on 17-5-17.
 */
public class ExternalDataSourceResqVO {
    //数据仓库id
    private Integer dhid;
    //数据库名称
    private String dbname;
    //表名称
    private String dtname;

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
}
