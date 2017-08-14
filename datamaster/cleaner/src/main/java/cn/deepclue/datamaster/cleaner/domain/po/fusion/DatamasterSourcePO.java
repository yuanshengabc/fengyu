package cn.deepclue.datamaster.cleaner.domain.po.fusion;

import java.util.Date;

/**
 * 星河数据源
 * Created by sunxingwen on 17-5-18.
 */
public class DatamasterSourcePO {
    //数据源id
    private Integer dsid;
    //名称
    private String name;
    //record source id
    private Integer rsid;
    //来源
    private String source;
    //描述
    private String description;
    //创建时间
    private Date createdOn;

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRsid() {
        return rsid;
    }

    public void setRsid(Integer rsid) {
        this.rsid = rsid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
