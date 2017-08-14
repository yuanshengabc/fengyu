package cn.deepclue.datamaster.streamer.session;

/**
 * Created by luoyong on 17-4-15.
 */
public class TableInfo {

    private String tableName;
    private Integer tableRows;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getTableRows() {
        return tableRows;
    }

    public void setTableRows(Integer tableRows) {
        this.tableRows = tableRows;
    }
}
