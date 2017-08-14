package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by xuzb on 27/03/2017.
 */
@Mapper
public interface DataHouseMapper {

    @Select("SELECT * FROM datahouse WHERE dhid = #{dhid}")
    DataHouse getDataHouse(@Param("dhid") int dhid);

    @Insert("INSERT INTO datahouse (dhid, uid, name, description, ip, port, username, password, modified_on) " +
            "VALUES(null, #{dataHouse.uid}, #{dataHouse.name}, #{dataHouse.description}, " +
            "#{dataHouse.ip}, #{dataHouse.port}, #{dataHouse.username}, #{dataHouse.password}, " +
            "current_timestamp)")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS dhid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "dataHouse.dhid",
            keyColumn = "dhid"
    )
    boolean insertDataHouse(@Param("uid") int uid, @Param("dataHouse") DataHouse dataHouse);

    @Delete("DELETE from datahouse WHERE uid = #{uid} AND dhid = #{dhid}")
    boolean deleteDataHouse(@Param("uid") int uid, @Param("dhid") int dhid);

    @Select("SELECT * FROM datahouse where uid = #{uid}")
    List<DataHouse> getDataHouses(@Param("uid")int uid);

    @Update("UPDATE datahouse SET name = #{dataHouse.name}, uid = #{dataHouse.uid},"
        + " description = #{dataHouse.description}, ip = #{dataHouse.ip}, "
        + " port = #{dataHouse.port}, username = #{dataHouse.username}, "
        + " password = #{dataHouse.password}, modified_on = #{now}"
        + " WHERE dhid = #{dataHouse.dhid}")
    boolean updateDataHouse(@Param("dataHouse") DataHouse dataHouse, @Param("now") Timestamp now);
}
