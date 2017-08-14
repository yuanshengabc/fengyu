package cn.deepclue.demo.app.dao;


import cn.deepclue.demo.app.domain.po.DemoPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.mapping.StatementType;

/**
 * Created by luoyong on 17-6-26.
 */
@Mapper
public interface DemoDao {
    @Select("select * from demo where id = #{id}")
    DemoPO getById(@Param("id") Long id);

    @Insert("INSERT INTO demo (column1, column2, column3, create_time, update_time) " +
            "VALUES (#{t.column1}, #{t.column2}, #{t.column3}, #{t.createTime}, #{t.updateTime})")
    @SelectKey(
            resultType = Long.class,
            statement = "SELECT LAST_INSERT_ID() AS id",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "t.id",
            keyColumn = "id"
    )
    int insert(@Param("t") DemoPO demoPO);
}
