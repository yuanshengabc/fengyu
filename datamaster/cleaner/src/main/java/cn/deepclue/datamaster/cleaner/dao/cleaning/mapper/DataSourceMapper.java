package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by magneto on 17-3-30.
 */
@Mapper
public interface DataSourceMapper {
    @Select("SELECT * FROM `datasource` WHERE dhid = #{dhid} and dbname = #{dbname} and dtname = #{dtname} limit 1")
    DataSource getDataSource(@Param("dhid") int dhid, @Param("dbname") String dbname,
                             @Param("dtname") String dtname);

    @Select("SELECT * FROM `datasource` WHERE dsid = #{dsid}")
    DataSource getDataSourceByDsid(@Param("dsid") int dsid);

    @Select("SELECT * FROM `datasource` WHERE dsid IN (SELECT sid FROM workspace_source where wsid = #{wsid})")
    DataSource getDataSourceByWsid(@Param("dsid") int wsid);

    @Select("SELECT * FROM `datasource` WHERE dhid = #{dhid} limit #{offset}, #{limit}")
    List<DataSource> getDataSourcesByDhid(@Param("dhid") int dhid, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO `datasource`(dsid, dhid, dbname, dtname, rsid, status, description, ntotal, created_on) " +
            "VALUES(null, #{ds.dhid}, #{ds.dbname}, #{ds.dtname}, " +
            "#{ds.rsid}, #{ds.status}, #{ds.description}, #{ds.ntotal}, " +
            "#{ds.createdOn})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS dsid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "ds.dsid",
            keyColumn = "dsid"
    )
    boolean insertDataSource(@Param("ds") DataSource dataSource);

    @Delete("DELETE from `datasource` WHERE dsid = #{dsid}")
    boolean deleteDataSource(@Param("dsid") int dsid);

    @Update("UPDATE `datasource` SET dtname = #{dataSource.dtname}, dbname = #{dataSource.dbname},"
            + " description = #{dataSource.description}, dhid = #{dataSource.dhid}, "
            + " rsid = #{dataSource.rsid}, status = #{dataSource.status}, ntotal = #{dataSource.ntotal} "
            + " WHERE dsid = #{dataSource.dsid}")
    boolean updateDataSource(@Param("dataSource") DataSource dataSource);

    @Select("SELECT * FROM `datasource` WHERE dsid = #{dsid}")
    @Results({
            @Result(id = true, property = "dsid", column = "{dsid}"),
            @Result(property = "dataHouse", column = "dhid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.DataHouseMapper.getDataHouse"))
    })
    DataSourceBO getDataSourceBO(@Param("dsid") int dsid);
}
