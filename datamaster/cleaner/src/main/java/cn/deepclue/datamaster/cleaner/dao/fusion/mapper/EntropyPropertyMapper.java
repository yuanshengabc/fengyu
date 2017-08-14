package cn.deepclue.datamaster.cleaner.dao.fusion.mapper;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.EntropyPropertyPO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by ggchangan on 17-5-23.
 */
@Mapper
public interface EntropyPropertyMapper {
    @Insert("INSERT INTO fusion_property(wsptid, wsid, ptid, `unique`) " +
            "VALUES(null, #{entropy.wsid}, #{entropy.ptid}, #{entropy.unique}) ")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS otid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "entropy.wsptid",
            keyColumn = "wsptid"
    )
    boolean insertObjectType(@Param("entropy") EntropyPropertyPO entropyPropertyPO);

    @Delete("DELETE from fusion_property WHERE wsid = #{fwsid}")
    Integer deleteByFwsid(@Param("fwsid") int fwsid);

    @Select("SELECT * from fusion_property WHERE wsid = #{fwsid}")
    List<EntropyPropertyPO> getAll(@Param("fwsid") int fwsid);
}
