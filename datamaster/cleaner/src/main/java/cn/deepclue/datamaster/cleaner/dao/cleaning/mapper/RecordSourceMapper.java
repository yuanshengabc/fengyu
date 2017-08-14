package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.model.schema.RSField;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by magneto on 17-4-5.
 */
@Mapper
public interface RecordSourceMapper {
    @Select("SELECT * FROM recordsource WHERE rsid = #{rsid}")
    RecordSource getRecordSource(@Param("rsid") int rsid);

    @Insert("INSERT INTO recordsource (rsid, name, dsid, type, created_on) " +
            "VALUES(null, #{recordSource.name}, #{recordSource.dsid}, #{recordSource.type}, " +
            "#{recordSource.createdOn})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS rsid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "recordSource.rsid",
            keyColumn = "rsid"
    )
    boolean insertRecordSource(@Param("recordSource") RecordSource recordSource);

    @Select("SELECT name, type from rsfield WHERE rsid = #{rsid}")
    List<RSField> getRSFields(@Param("rsid") int rsid);

    @Insert({"<script>",
            "INSERT INTO rsfield (rsid, name, type)",
            "VALUES ",
            "<foreach  collection='rsfields' item='rsfield' separator=','>",
            "(#{rsid, jdbcType = INTEGER}, #{rsfield.name,jdbcType=VARCHAR}, #{rsfield.type,jdbcType=INTEGER})",
            "</foreach>",
            "</script>"
    })
    int insertRSFields(@Param("rsid") int rsid, @Param("rsfields") List<RSField> fields);

    @Delete("DELETE from rsfield WHERE rsid = #{rsid}")
    boolean deleteRSFields(@Param("rsid") int rsid);

    @Delete("DELETE FROM recordsource where rsid = #{rsid}")
    boolean deleteRecordSource(@Param("rsid") int rsid);
}
