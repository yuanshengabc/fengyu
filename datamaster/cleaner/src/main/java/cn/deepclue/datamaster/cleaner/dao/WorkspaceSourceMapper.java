package cn.deepclue.datamaster.cleaner.dao;

import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by magneto on 17-3-28.
 */
@Mapper
public interface WorkspaceSourceMapper {
    @Select("SELECT * FROM `database` WHERE dhid = #{dhid} and name = #{name}")
    Database getDatabase(@Param("dhid") int dhid, @Param("name") String name);

    @Select("SELECT * FROM `database` where dhid = #{dhid} limit #{offset}, #{limit}")
    List<Database> getDatabases(@Param("dhid") int dhid, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO `workspace_source`(wsdsid, wsid, sid, stype, created_on) VALUES (null, #{wsds.wsid}, #{wsds.sid}, #{wsds.stype}, #{wsds.createdOn});")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS wsdsid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "wsds.wsdsid",
            keyColumn = "wsdsid"
    )
    Boolean insertWorkspaceDataSource(@Param("wsds") WorkspaceSourcePO workspaceSourcePO);

    @Delete("DELETE from `workspace_source` WHERE wsid = #{wsid}")
    Integer deleteByWsid(@Param("wsid") int wsid);

    @Update("TRUNCATE TABLE `database`")
    boolean truncate();

    List<Database> deleteDatabases(int dhid);

    @Select("SELECT * FROM `workspace_source` where wsid = #{wsid}")
    List<WorkspaceSourcePO> getWorkspaceDataSourcesByWsid(@Param("wsid") int wsid);

    @Select("SELECT COUNT(*) FROM `workspace_source` where wsid = #{wsid}")
    int getSourcesCount(@Param("wsid") int fwsid);

    @Select("select dt.*, dt1.match from workspace_source as dt left join ontology_source as dt1 on dt.wsdsid = dt1.wsdsid where stype = 0 and wsid = #{fwsid}")
    @Results({
            @Result(id = true, property = "wsdsid", column = "wsdsid"),
            @Result(property = "dataSourceBO", column = "sid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.DataSourceMapper.getDataSourceBO"))
    })
    List<FusionDataSourceBO> getFusionDataSources(@Param("fwsid") int fswid);

    @Select("select dt.*, dt1.match from workspace_source as dt left join ontology_source as dt1 on dt.wsdsid = dt1.wsdsid where stype = 1 and wsid = #{fwsid}")
    @Results({
            @Result(id = true, property = "wsdsid", column = "wsdsid"),
            @Result(property = "datamasterSourcePO", column = "sid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.fusion.mapper.DatamasterSourceMapper.getDatamasterSource"))
    })
    List<FusionDatamasterSourceBO> getFusionDatamasterSources(@Param("fwsid") int fwsid);

    @Select("select * from workspace_source where wsid = #{wsid} and sid = #{sid} and stype = #{stype}")
    WorkspaceSourcePO getWorkspaceSource(@Param("wsid") Integer wsid, @Param("sid") Integer sid, @Param("stype") Integer stype);

    @Delete("DELETE from `workspace_source` WHERE wsid = #{wsid} and sid = #{sid} and stype = #{stype}")
    boolean delete(@Param("wsid") int wsid, @Param("sid") int sid, @Param("stype") int stype);

    @Select("select dt1.multimatch_ptids from workspace_source as dt left join ontology_source as dt1 on dt.wsdsid = dt1.wsdsid where wsid = #{fwsid} and dt1.multimatch_ptids is not null")
    List<String> getMultiMatchPtids(@Param("fwsid") int fwsid);
}
