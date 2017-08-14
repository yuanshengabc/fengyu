package cn.deepclue.demo.appa.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DemoDao {

    @Select("SELECT description From `demo` WHERE id = #{id}")
    String getDescById(@Param("id") int id);
}
