package cn.deepclue.datamaster.cleaner.dao.typehandler;

import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressCodeTypeHandler implements TypeHandler<AddressCodeType> {
    @Override
    public void setParameter(PreparedStatement ps, int i, AddressCodeType parameter, JdbcType jdbcType) throws SQLException {
        //Not Need
    }

    @Override
    public AddressCodeType getResult(ResultSet rs, String columnName) throws SQLException {
        return AddressCodeType.getAddressCodeType(rs.getInt(columnName));
    }

    @Override
    public AddressCodeType getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public AddressCodeType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
