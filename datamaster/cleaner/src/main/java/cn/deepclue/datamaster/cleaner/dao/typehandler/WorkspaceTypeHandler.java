package cn.deepclue.datamaster.cleaner.dao.typehandler;

import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by magneto on 17-5-18.
 */
public class WorkspaceTypeHandler implements TypeHandler<WorkspaceType> {
    @Override
    public void setParameter(PreparedStatement ps, int i, WorkspaceType parameter, JdbcType jdbcType) throws SQLException {
        //Not Need
    }

    @Override
    public WorkspaceType getResult(ResultSet rs, String columnName) throws SQLException {
        return WorkspaceType.getWorkspaceType(rs.getInt(columnName));
    }

    @Override
    public WorkspaceType getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public WorkspaceType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
