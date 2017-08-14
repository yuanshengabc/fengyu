package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

/**
 * Created by magneto on 17-6-6.
 */
public class FusionKeyDataSourceVO {
    private Integer dsid;
    private String ip;
    private String username;
    private String password;
    private String dbname;
    private String dtname;

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
