package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.po.cleaning.TransformationPO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by xuzb on 05/04/2017.
 */
@Mapper
public interface TransformationMapper {
    @Select("SELECT * FROM transformation WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    List<TransformationPO> getTransformations(@Param("wsid") int wsid, @Param("wsversion") int wsversion);

    @Delete("DELETE FROM transformation WHERE tfid = #{tfid}")
    boolean deleteTransformation(@Param("tfid") int tfid);

    @Insert("INSERT INTO transformation (tfid, tftype, wsid, wsversion, args, filters) " +
            "VALUES(null, #{tf.tftype}, #{tf.wsid}, #{tf.wsversion}, #{tf.args}, #{tf.filters})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS tfid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "tf.tfid",
            keyColumn = "tfid"
    )

    boolean insertTransformation(@Param("tf") TransformationPO tf);
}
