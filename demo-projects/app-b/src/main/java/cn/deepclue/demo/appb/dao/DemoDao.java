package cn.deepclue.demo.appb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DemoDao {

    @Select("SELECT id From `demo` WHERE description = #{description}")
    int getIdbyDescribe(@Param("description") String description);
}
