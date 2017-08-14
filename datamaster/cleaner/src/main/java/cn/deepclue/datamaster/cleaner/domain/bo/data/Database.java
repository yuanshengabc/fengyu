package cn.deepclue.datamaster.cleaner.domain.bo.data;

/**
 * Created by xuzb on 27/03/2017.
 */
public class Database {
    private Integer dbid;
    private String name;
    private Integer dhid;

    public String getName() {
        return name;
    }

    public void setName(String dbname) {
        this.name = dbname;
    }

    public Integer getDbid() {
        return dbid;
    }

    public void setDbid(Integer dbid) {
        this.dbid = dbid;
    }

    public Integer getDhid() {
        return dhid;
    }

    public void setDhid(Integer dhid) {
        this.dhid = dhid;
    }
}
