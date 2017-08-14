package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.vo.ontology.PropertyTypeVO;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.model.ontology.PropertyGroup;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by magneto on 17-3-31.
 */
@Mapper
public interface OntologyMapper {
    /**
     * objectType相关接口
     **/
    @Select("SELECT * FROM objecttype ORDER BY otid DESC limit #{offset}, #{limit}")
    List<ObjectType> getObjectTypes(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO objecttype(otid, name, description) " +
            "VALUES(null, #{objectType.name}, #{objectType.description}) ")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS otid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "objectType.otid",
            keyColumn = "otid"
    )
    boolean insertObjectType(@Param("objectType") ObjectType objectType);

    @Select("SELECT * FROM objecttype where otid = #{otid}")
    ObjectType getObjectType(@Param("otid") int otid);

    @Select("SELECT * FROM objecttype where otid = #{otid}")
    @Results({
            @Result(id = true, property = "otid", column = "otid"),
            @Result(property = "propertyTypes", column = "otid", many = @Many(select = "getAllPropertyTypes"))
    })
    ObjectTypeBO getObjectTypeBO(@Param("otid") int otid);

    @Update("UPDATE objecttype SET name = #{objectType.name}, description = #{objectType.description}"
            + " WHERE otid = #{objectType.otid}")
    Boolean updateObjectType(@Param("objectType") ObjectType objectType);

    @Delete("DELETE from objecttype WHERE otid = #{otid}")
    Boolean deleteObjectType(@Param("otid") int otid);

    /**
     * propertyType相关接口
     **/
    @Select("SELECT * FROM propertytype where otid = #{otid} ORDER BY ${orderBy} DESC limit #{offset}, #{limit}")
    List<PropertyType> getPropertyTypes(@Param("otid") int otid, @Param("offset") int offset,
                                        @Param("limit") int limit, @Param("orderBy") String orderBy);

    @Select("SELECT * FROM propertytype where otid = #{otid}")
    List<PropertyType> getAllPropertyTypes(@Param("otid") int otid);

    @Insert("INSERT INTO propertytype(ptid, otid, name, semaName, description, pgid, baseType, validationRule) " +
            "VALUES(null, #{propertyType.otid}, #{propertyType.name}, #{propertyType.semaName}, #{propertyType.description}, "
            + "#{propertyType.pgid}, #{propertyType.baseType}, #{propertyType.validationRule}) ")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS ptid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "propertyType.ptid",
            keyColumn = "ptid"
    )
    boolean insertPropertyType(@Param("propertyType") PropertyType propertyType);

    @Update("UPDATE propertytype SET name = #{propertyType.name}, semaName = #{propertyType.semaName},"
            + " description = #{propertyType.description}, pgid = #{propertyType.pgid}, "
            + " baseType = #{propertyType.baseType}, validationRule = #{propertyType.validationRule} "
            + " WHERE ptid = #{propertyType.ptid}")
    boolean updatePropertyType(@Param("propertyType") PropertyType propertyType);


    @Select("SELECT * FROM propertytype where ptid = #{ptid}")
    PropertyType getPropertyType(@Param("ptid") int ptid);

    @Select("SELECT * FROM propertytype where ptid = #{ptid}")
    @Results({
            @Result(id = true, column = "ptid", property = "ptid"),
            @Result(property = "propertyGroup", column = "pgid", one = @One(select = "getPropertyGroup"))
    })
    PropertyTypeVO getPropertyTypeVO(@Param("ptid") int ptid);

    @Delete("DELETE from propertytype WHERE ptid = #{ptid}")
    boolean deletePropertyType(@Param("ptid") int ptid);

    /**
     * propertyGroup相关接口
     **/
    @Update("INSERT INTO propertygroup(pgid, name, otid) VAlUES (null, #{propertyGroup.name}, #{propertyGroup.otid})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS pgid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "propertyGroup.pgid",
            keyColumn = "pgid"
    )
    boolean insertPropertyGroup(@Param("propertyGroup") PropertyGroup propertyGroup);

    @Select("SELECT * FROM propertygroup where pgid = #{pgid}")
    PropertyGroup getPropertyGroup(@Param("pgid") int pgid);

    @Update("UPDATE propertygroup SET name = #{propertyGroup.name}, "
            + " otid = #{propertyGroup.otid} WHERE pgid = #{propertyGroup.pgid}")
    boolean updatePropertyGroup(@Param("propertyGroup") PropertyGroup propertyGroup);

    @Delete("DELETE from propertygroup WHERE pgid = #{pgid}")
    boolean deletePropertyGroup(@Param("pgid") int pgid);

    @Select("SELECT * FROM propertytype where pgid = #{pgid}")
    List<PropertyType> getPropertyTypesOfGroup(@Param("pgid") int pgid);

    @Select("SELECT COUNT(*) FROM propertytype where otid = #{otid}")
    Integer getPropertyTypeCount(@Param("otid") int otid);

    @Select("SELECT COUNT(*) FROM propertytype where pgid = #{pgid}")
    Integer getPropertyTypeCountOfGroup(@Param("pgid") int pgid);

    @Select("SELECT COUNT(*) FROM propertytype where otid = #{otid} AND pgid is null")
    Integer getPropertyTypeCountOfUnGroup(@Param("otid") int otid);

    @Select("SELECT * FROM propertytype where otid = #{otid} AND pgid = #{pgid} ORDER BY ${orderBy} DESC limit #{offset}, #{limit}")
    List<PropertyType> getPropertyTypesOfGroupOnePage(@Param("otid") int otid, @Param("pgid") int pgid,
                                                      @Param("offset") int offset, @Param("limit") int limit, @Param("orderBy") String orderBy);

    @Select("SELECT * FROM propertytype where otid = #{otid} AND pgid is null ORDER BY ${orderBy} DESC limit #{offset}, #{limit}")
    List<PropertyType> getPropertyTypesOfUnGrouped(@Param("otid") int otid,
                                                   @Param("offset") int offset,
                                                   @Param("limit") int limit,
                                                   @Param("orderBy") String orderBy);

    @Select("SELECT * FROM propertygroup where otid = #{otid}")
    List<PropertyGroup> getPropertyGroups(@Param("otid") int otid);

    @Select("SELECT COUNT(*) FROM objecttype")
    Integer getObjectTypeCount();

}
