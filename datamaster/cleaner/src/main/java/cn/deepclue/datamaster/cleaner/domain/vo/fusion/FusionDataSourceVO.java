package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;

import java.util.Date;

/**
 * 融合空间已选择的数据源ListVO
 * Created by magneto on 17-5-17.
 */
public class FusionDataSourceVO {
    //数据源id
    private Integer dsid;
    //数据名称
    private String name;
    //类型
    private SourceType type;
    //数据仓库id
    private Integer dhid;
    //数据仓库名称
    private String dhname;
    //服务器ip
    private String ip;
    //端口
    private Integer port;
    //数据库
    private String dbname;
    //是否匹配
    private Boolean status;
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

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public Integer getDhid() {
        return dhid;
    }

    public void setDhid(Integer dhid) {
        this.dhid = dhid;
    }

    public String getDhname() {
        return dhname;
    }

    public void setDhname(String dhname) {
        this.dhname = dhname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public FusionDataSourceVO fromBO(FusionDataSourceBO fusionDataSourceBO) {
        DataSourceBO dataSourceBO = fusionDataSourceBO.getDataSourceBO();
        this.setDsid(dataSourceBO.getDsid());
        this.setName(dataSourceBO.getDtname());
        this.setType(SourceType.DATASOURCE);
        DataHouse dataHouse = dataSourceBO.getDataHouse();
        if (dataHouse != null) {
            this.setDhid(dataHouse.getDhid());
            this.setDhname(dataHouse.getName());
            this.setIp(dataHouse.getIp());
            this.setPort(dataHouse.getPort());
        }
        this.setDbname(dataSourceBO.getDbname());
        this.setCreatedOn(fusionDataSourceBO.getCreatedOn());
        this.setStatus(fusionDataSourceBO.getMatch());
        return this;
    }

    public FusionDataSourceVO fromBO(FusionDatamasterSourceBO fusionDatamasterSourceBO) {
        DatamasterSourcePO datamasterSourcePO = fusionDatamasterSourceBO.getDatamasterSourcePO();
        this.setDsid(datamasterSourcePO.getDsid());
        this.setName(datamasterSourcePO.getName());
        this.setType(SourceType.DATAMASTER_SOURCE);
        this.setCreatedOn(fusionDatamasterSourceBO.getCreatedOn());
        this.setStatus(fusionDatamasterSourceBO.getMatch());
        return this;
    }
}
