package cn.deepclue.datamaster.cleaner.domain.bo.data;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * Created by xuzb on 15/03/2017.
 */
public class DataHouse {
    private Integer dhid;
    @NotBlank(message = "ip can not be null")
    private String ip;
    private int port;
    private String name;
    @NotBlank(message = "username can not be null")
    private String username;
    @NotBlank(message = "password can not be null")
    private String password;
    private Date createdOn;
    private String description;
    private Date modifiedOn;
    private int uid;

    public Integer getDhid() {
        return dhid;
    }

    public void setDhid(Integer dhid) {
        this.dhid = dhid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
