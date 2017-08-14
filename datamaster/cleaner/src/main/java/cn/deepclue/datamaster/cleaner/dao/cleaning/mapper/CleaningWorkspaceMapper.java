package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.OntologyMapping;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.OntologyMappingPO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.WorkspaceEditionPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by magneto on 17-5-18.
 */
@Mapper
public interface CleaningWorkspaceMapper {

    @Select("SELECT * FROM workspace_edition WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    @Results({
            @Result(property = "objectType", column = "otid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper.getObjectType")),
            @Result(property = "importTask", column = "importid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "analysisTask", column = "analysisid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "transformTask", column = "transformid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "exportTask", column = "exportid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask"))
    })
    WorkspaceEdition getWorkspaceEdition(@Param("wsid") int wsid, @Param("wsversion") int wsversion);

    @Select("SELECT * FROM workspace_edition WHERE wsid = #{wsid}")
    @Results({
            @Result(property = "objectType", column = "otid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper.getObjectType")),
            @Result(property = "importTask", column = "importid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "analysisTask", column = "analysisid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "transformTask", column = "transformid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask")),
            @Result(property = "exportTask", column = "exportid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper.getTask"))
    })
    List<WorkspaceEdition> getWorkspaceEditions(int wsid);

    @Insert("INSERT INTO workspace_edition(wsid, wsversion, rsid, otid, modified_on, importid, analysisid, transformid, exportid) VALUES " +
            "(#{edition.wsid}, #{edition.wsversion}, #{edition.rsid}, #{edition.otid}, " +
            "#{edition.modifiedOn}, #{edition.importTask.tid}, #{edition.analysisTask.tid}, #{edition.transformTask.tid}, #{edition.exportTask.tid})")
    boolean insertWorkspaceEdition(@Param("edition") WorkspaceEditionPO workspaceEditionPO);

    @Update("UPDATE workspace_edition SET rsid = #{edition.rsid}, otid = #{edition.otid}, modified_on = #{edition.modifiedOn}," +
            "importid = #{edition.importTask.tid}, analysisid = #{edition.analysisTask.tid}, transformid = #{edition.transformTask.tid}, exportid = #{edition.exportTask.tid} " +
            " WHERE wsid = #{edition.wsid} AND wsversion = #{edition.wsversion}")
    boolean updateWorkspaceEdition(@Param("edition") WorkspaceEditionPO workspaceEditionPO);

    @Select("SELECT * from ontology_mapping WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    @Results({
            @Result(id = true, column = "omid", property = "omid"),
            @Result(column = "field_name", property = "fieldName"),
            @Result(property = "propertyType", column = "ptid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper.getPropertyTypeVO"))
    })
    List<OntologyMapping> getOntologyMappings(@Param("wsid") int wsid, @Param("wsversion") int wsversion);

    @Select("SELECT * FROM ontology_mapping WHERE wsid = #{wsid} AND wsversion = #{wsversion} AND field_name = #{fieldName}")
    @Results({
            @Result(id = true, column = "omid", property = "omid"),
            @Result(column = "field_name", property = "fieldName"),
            @Result(property = "propertyType", column = "ptid", one = @One(select = "cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper.getPropertyTypeVO"))
    })
    OntologyMapping getOntologyMapping(@Param("wsid") Integer wsid, @Param("wsversion") Integer wsversion, @Param("fieldName") String fieldName);

    @Update("UPDATE ontology_mapping SET ptid = #{om.ptid} WHERE wsid = #{om.wsid} AND wsversion = #{om.wsversion} AND field_name = #{om.fieldName}")
    boolean updateOntologyMapping(@Param("om") OntologyMappingPO ontologyMapping);

    @Insert("INSERT INTO ontology_mapping(wsid, wsversion, field_name, ptid) " +
            "VALUES (#{om.wsid}, #{om.wsversion}, #{om.fieldName}, #{om.ptid})")
    boolean insertOntologyMapping(@Param("om") OntologyMappingPO ontologyMapping);

    @Delete("DELETE from ontology_mapping WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    boolean deleteOntologyMappings(@Param("wsid") int wsid, @Param("wsversion") int wsversion);

    @Delete("DELETE from ontology_mapping WHERE wsid = #{wsid} AND wsversion = #{wsversion} AND field_name = #{fieldName}")
    boolean deleteOntologyMapping(@Param("wsid") int wsid, @Param("wsversion") int wsversion, @Param("fieldName") String fieldName);

    @Update("UPDATE cleaning_workspace SET wsversion = wsversion + 1 WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    boolean upgradeWorkspaceVersion(@Param("wsid") int wsid, @Param("wsversion") int wsversion);

    @Update("DELETE from workspace_edition where wsid = #{wsid}")
    boolean deleteWorkspaceEditions(@Param("wsid") int wsid);

    @Select("SELECT * from ontology_mapping WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    @Results({
            @Result(column = "field_name", property = "fieldName")
    })
    List<OntologyMappingPO> getOntologyMappingPOs(@Param("wsid") int wsid, @Param("wsversion") int wsversion);

    @Insert({"<script>",
            "INSERT INTO ontology_mapping (wsid, wsversion, field_name, ptid)",
            "VALUES ",
            "<foreach  collection='oms' item='om' separator=','>",
            "(#{om.wsid, jdbcType = INTEGER}, #{om.wsversion, jdbcType = INTEGER}, " +
                    "#{om.fieldName, jdbcType = VARCHAR}, #{om.ptid,jdbcType=INTEGER})",
            "</foreach>",
            "</script>"
    })
    int insertOntologyMappings(@Param("oms") List<OntologyMappingPO> ontologyMappings);

    @Update("UPDATE workspace_edition SET otid = #{otid} WHERE wsid = #{wsid} AND wsversion = #{wsversion}")
    boolean bindOntology(@Param("wsid") int wsid, @Param("wsversion") int wsversion, @Param("otid") Integer otid);
}
