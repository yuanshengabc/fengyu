package cn.deepclue.datamaster.testio.bean;

/**
 * Created by lilei-mac on 2017/4/23.
 */
public class IOBean {

    private Integer recordNumber;

    private Integer recordColumn;

    private String importTableName;

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public Integer getRecordColumn() {
        return recordColumn;
    }

    public void setRecordColumn(Integer recordColume) {
        this.recordColumn = recordColume;
    }

    public String getImportTableName() {
        return importTableName;
    }

    public void setImportTableName(String importTableName) {
        this.importTableName = importTableName;
    }
}
