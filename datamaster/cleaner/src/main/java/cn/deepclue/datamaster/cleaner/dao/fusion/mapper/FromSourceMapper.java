package cn.deepclue.datamaster.cleaner.dao.fusion.mapper;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.FromSourcePO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by magneto on 17-6-6.
 */
@Mapper
public interface FromSourceMapper {

    @Select("select * from from_source where dsid = #{dmsid}")
    List<FromSourcePO> getFromSources(@Param("dmsid") Integer dmsid);

    @Insert("INSERT INTO from_source(fdsid, dsid, `from`, stype) " +
            "VALUES(null, #{from.dsid}, #{from.from}, #{from.stype}) ")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS fdsid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "from.fdsid",
            keyColumn = "fdsid"
    )
    boolean insert(@Param("from") FromSourcePO fromSourcePO);

    @Select("select * from from_source")
    List<FromSourcePO> getAllFromSources();
}
