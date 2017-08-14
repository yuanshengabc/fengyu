package cn.deepclue.demo.app.domain.po;

import java.util.Date;

/**
 * Created by luoyong on 17-6-26.
 */
public class DemoPO {
    private Long id;
    private String column1;
    private Double column2;
    private Double column3;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public Double getColumn2() {
        return column2;
    }

    public void setColumn2(Double column2) {
        this.column2 = column2;
    }

    public Double getColumn3() {
        return column3;
    }

    public void setColumn3(Double column3) {
        this.column3 = column3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
