package cn.deepclue.datamaster.cleaner.dao;

import cn.deepclue.datamaster.cleaner.dao.typehandler.AddressCodeTypeHandler;
import cn.deepclue.datamaster.cleaner.dao.typehandler.WorkspaceTypeHandler;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.CleaningWorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by xuzb on 31/03/2017.
 */
@Mapper
public interface WorkspaceMapper {
    @Insert("INSERT INTO workspace (uid, name, description, wstype, finished, created_on) " +
            "VALUES (#{ws.uid}, #{ws.name}, #{ws.description}, #{ws.wstype}, #{ws.finished}, #{ws.createdOn})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS wsid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "ws.wsid",
            keyColumn = "wsid"
    )
    boolean insertWorkspace(@Param("ws") WorkspacePO workspacePO);

    @Insert("INSERT INTO cleaning_workspace (wsid, wsversion) " +
            "VALUES (#{ws.wsid}, #{ws.wsversion})")
    boolean insertCleaningWorkspace(@Param("ws") CleaningWorkspacePO cleaningWorkspacePO);

    @Insert("INSERT INTO workspace_source (wsid, sid, stype) " +
            "VALUES (#{ws.wsid}, #{ws.sid}, #{ws.stype})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS wsdsid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "ws.wsdsid",
            keyColumn = "wsdsid"
    )
    boolean insertWorkspaceSource(@Param("ws") WorkspaceSourcePO workspaceSourcePO);

    @Insert("INSERT INTO fusion_workspace (wsid, step, rsid) " +
            "VALUES (#{ws.wsid}, #{ws.step}, #{ws.rsid})")
    boolean insertFusionWorkspace(@Param("ws") FusionWorkspacePO fusionWorkspacePO);

    @Select({"<script>",
            "SELECT count(*) from workspace",
            "<where>",
            "<if test='wstype != null'> wstype = #{wstype}</if>",
            "<if test='finished != null'> and finished = #{finished}</if>",
            "<if test=\"keyword != null and keyword != ''\"> and name like #{keyword}</if>",
            "</where>",
            "</script>"
    })
    Integer getWorkspacesCount(@Param("wstype") Integer wstype, @Param("finished") Integer finished, @Param("keyword") String keyword);

    @Select({"<script>",
            "SELECT * FROM workspace",
            "<where>",
            "<if test='wstype != null'> wstype = #{wstype}</if>",
            "<if test='finished != null'> and finished = #{finished}</if>",
            "<if test=\"keyword != null and keyword != ''\"> and name like #{keyword}</if>",
            "</where>",
            " order by wsid desc",
            " limit #{offset}, #{limit}",
            "</script>"
    })
    List<WorkspacePO> getWorkspaces(@Param("offset") int offset, @Param("limit") int limit, @Param("wstype") Integer wstype, @Param("finished") Integer finished, @Param("keyword") String keyword);

    @Select("SELECT * FROM workspace, cleaning_workspace WHERE workspace.wsid = #{wsid} AND cleaning_workspace.wsid = #{wsid}")
    @Results(value = {
            @Result(id = true, column = "wsid", property = "wsid"),
            @Result(property = "wstype", column = "wstype", typeHandler = WorkspaceTypeHandler.class),
            @Result(property = "edition", column = "{wsid = wsid, wsversion = wsversion}", one = @One(select = "getWorkspaceEdition")),
            @Result(property = "dataSource", column = "wsid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.DataSourceMapper.getDataSourceByWsid"))
    })
    CleaningWorkspaceBO getCleaningWorkspace(@Param("wsid") int wsid);

    @Select("SELECT * FROM workspace, fusion_workspace WHERE workspace.wsid = #{wsid} AND fusion_workspace.wsid = #{wsid}")
    @Results(value = {
            @Result(property = "addressCodeType", column = "addressCodeType", typeHandler = AddressCodeTypeHandler.class),
            @Result(property = "wstype", column = "wstype", typeHandler = WorkspaceTypeHandler.class),
            @Result(property = "objectTypeBO", column = "otid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper.getObjectTypeBO")),
            @Result(property = "entropyCalculationTask", column = "entropyCalculationTid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "similarityTask", column = "fusionTid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "recordSource", column = "rsid",
                    one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.RecordSourceMapper.getRecordSource"))
    })
    FusionWorkspaceBO getFusionWorkspace(@Param("wsid") int wsid);

    @Update("UPDATE workspace SET name = #{name}, description = #{description} WHERE wsid = #{wsid}")
    boolean updateWorkspace(@Param("wsid") int wsid, @Param("name") String name, @Param("description") String description);

    @Update("DELETE from workspace where wsid = #{wsid}")
    boolean deleteWorkspace(@Param("wsid") int wsid);

    @Delete("DELETE workspace, fusion_workspace from workspace, fusion_workspace where workspace.wsid = #{wsid} AND fusion_workspace.wsid = #{wsid}")
    boolean deleteFusionWorkspace(@Param("wsid") int wsid);

    @Delete("DELETE workspace, cleaning_workspace from workspace, cleaning_workspace where workspace.wsid = #{wsid} AND cleaning_workspace.wsid = #{wsid}")
    boolean deleteCleaningWorkspace(@Param("wsid") int wsid);

    @Select("SELECT * FROM workspace WHERE wsid = #{wsid}")
    WorkspacePO getWorkspace(@Param("wsid") int wsid);

    @Select("SELECT * FROM workspace_edition WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    @Results({
            @Result(property = "objectType", column = "otid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper.getObjectType")),
            @Result(property = "importTask", column = "importid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "analysisTask", column = "analysisid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "transformTask", column = "transformid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "exportTask", column = "exportid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask"))
    })
    WorkspaceEdition getWorkspaceEdition(@Param("wsid") int wsid, @Param("wsversion") int wsversion);

    @Update("UPDATE workspace SET finished = #{finished}, modified_on = now() WHERE wsid = #{wsid}")
    boolean updateWorkspaceStatus(@Param("wsid") int wsid, @Param("finished") int finished);
}
