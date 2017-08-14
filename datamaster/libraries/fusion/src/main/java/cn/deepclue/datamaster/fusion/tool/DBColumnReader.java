package cn.deepclue.datamaster.fusion.tool;

import cn.deepclue.datamaster.streamer.io.SchemaConverter;

import java.io.IOException;
import java.sql.*;

/**
 * Created by liuanhao on 2017/7/12.
 */
public class DBColumnReader {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String url = args[0];
        String userName = args[1];
        String password = args[2];
        String tableName = args[3];

        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);

            ptmt = conn.prepareStatement(
                    String.format("select column_name from information_schema.columns " +
                            "where table_name = '%s'", tableName)
            );

            rs = ptmt.executeQuery();

            while (rs.next()) {
                String originalColumnName = rs.getString("column_name");
                String base32ColumnName = SchemaConverter.base32Encode(originalColumnName);
                System.out.println(base32ColumnName);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (ptmt != null) {
                ptmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
    }
}
