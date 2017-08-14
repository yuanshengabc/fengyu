package cn.deepclue.datamaster.cleaner.dao.fusion.mapper;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by magneto on 17-5-17.
 */
@Mapper
public interface FusionWorkspaceMapper {
    @Update("UPDATE fusion_workspace SET otid = #{otid} WHERE wsid = #{fwsid}")
    boolean updateObjectType(@Param("fwsid") int fwsid, @Param("otid") Integer otid);

    @Update("UPDATE fusion_workspace SET addressCodeType = #{addressCodeType} WHERE wsid = #{fwsid}")
    boolean updateAddressCodeType(@Param("fwsid") int fwsid, @Param("addressCodeType") Integer addressCodeType);

    @Select("select * from fusion_workspace where wsid = #{fwsid}")
    FusionWorkspacePO getFusionWorkspace(@Param("fwsid") int fswid);

    @Update("UPDATE fusion_workspace SET threshold = #{threshold, jdbcType=DOUBLE, javaType=double} WHERE wsid = #{fwsid}")
    boolean updateThreshold(@Param("fwsid") int fwsid, @Param("threshold") Double threshold);

    @Update("UPDATE fusion_workspace SET step = #{step} WHERE wsid = #{fwsid}")
    boolean updateStep(@Param("fwsid") int fwsid, @Param("step") int step);

    @Update("UPDATE fusion_workspace SET fusionTid = #{tid} WHERE wsid = #{fwsid}")
    boolean updateSimilarityTask(@Param("fwsid") int fwsid, @Param("tid") Integer tid);

    @Update("UPDATE fusion_workspace SET entropyCalculationTid = #{tid} WHERE wsid = #{fwsid}")
    boolean updateEntropyCalculationTask(@Param("fwsid") int fwsid, @Param("tid") Integer tid);

    @Select("select * from fusion_workspace where rsid = #{rsid}")
    FusionWorkspacePO getFusionWorkspaceByRsid(@Param("rsid") int rsid);
}
