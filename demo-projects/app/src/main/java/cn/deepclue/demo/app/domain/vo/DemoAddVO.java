package cn.deepclue.demo.app.domain.vo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;


/**
 * Created by luoyong on 17-6-30.
 */

public class DemoAddVO {
    @NotBlank(message = "column1 不能为空")
    private String column1;
    @NotNull(message = "column2 不能为空")
    private Double column2;
    @NotNull(message = "column3　不能为空")
    @Range(min = 1, max = 100, message = "column3 必须大于１小于100 ")
    private Double column3;

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
}
