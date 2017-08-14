package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by magneto on 17-3-28.
 */
@Mapper
public interface DatabaseMapper {
    @Select("SELECT * FROM `database` WHERE dhid = #{dhid} and name = #{name}")
    Database getDatabase(@Param("dhid") int dhid, @Param("name") String name);

    @Select("SELECT * FROM `database` where dhid = #{dhid} limit #{offset}, #{limit}")
    List<Database> getDatabases(@Param("dhid") int dhid, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO `database`(dbid, dhid, name) VALUES (null, #{database.dhid}, #{database.name});")
    @SelectKey(
        resultType = Integer.class,
        statement = "SELECT LAST_INSERT_ID() AS dbid",
        statementType = StatementType.STATEMENT,
        before = false,
        keyProperty = "database.dbid",
        keyColumn = "dbid"
    )
    Boolean insertDatabase(@Param("database") Database database);

    @Delete("DELETE from `database` WHERE dhid = #{dhid} AND name = #{name}")
    boolean deleteDatabase(@Param("dhid") int dhid, @Param("name") String name);

    @Update("TRUNCATE TABLE `database`")
    boolean truncate();

    List<Database> deleteDatabases(int dhid);
}
