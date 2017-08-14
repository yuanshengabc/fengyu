package com.yuansheng.dao;

import org.apache.ibatis.annotations.*;


@Mapper
public interface SpringControlDao {
    @Select("SELECT * FROM person WHERE id = #{id}")
    String getName(@Param("id") int id);
}
