package cn.deepclue.datamaster.cleaner.dao.fusion.mapper;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by sunxingwen on 17-5-18.
 */
@Mapper
public interface DatamasterSourceMapper {
    @Select("SELECT count(*) FROM `datamaster_source`")
    Integer getDatamasterSourceCount();

    @Select("SELECT * FROM `datamaster_source` order by created_on desc limit #{offset}, #{limit}")
    @Results({
            @Result(id = true, property = "dsid", column = "dsid"),
            @Result(property = "createdOn", column = "created_on")
    })
    List<DatamasterSourcePO> getDatamasterSources(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT * FROM `datamaster_source` where dsid = #{dsid}")
    DatamasterSourcePO getDatamasterSource(@Param("dsid") int dsid);

    @Insert("INSERT INTO datamaster_source (name, rsid, source, description, created_on) " +
            "VALUES (#{dms.name}, #{dms.rsid}, #{dms.source}, #{dms.description}, #{dms.createdOn})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS dsid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "dms.dsid",
            keyColumn = "dsid"
    )
    boolean insertDatamasterSource(@Param("dms") DatamasterSourcePO datamasterSourcePO);

    @Delete("DELETE from `datamaster_source` WHERE dsid = #{dsid}")
    boolean deleteDatamasterSource(@Param("dsid") int dsid);

    @Select("select * from datamaster_source where dsid = #{dsid}")
    DatamasterSourcePO getDatamasterSourceByDsid(@Param("dsid") int dsid);

    @Select({"<script>",
            "select * from datamaster_source where dsid in ",
            "<foreach collection='dsids' index='index' item='item' open='(' separator=',' close=')'> ",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    List<DatamasterSourcePO> getDatamasterSourcesByDsids(@Param("dsids") List<Integer> dsids);

    @Select("SELECT * FROM `datamaster_source` where rsid = #{rsid}")
    List<DatamasterSourcePO> getDatamasterSourceByRsid(@Param("rsid") int rsid);

    @Select("SELECT * FROM `datamaster_source` order by created_on desc")
    List<DatamasterSourcePO> getAllDatamasterSources();
}
