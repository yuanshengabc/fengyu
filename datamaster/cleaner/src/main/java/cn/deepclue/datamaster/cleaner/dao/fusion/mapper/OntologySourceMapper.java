package cn.deepclue.datamaster.cleaner.dao.fusion.mapper;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.OntologySourcePO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by magneto on 17-5-23.
 */
@Mapper
public interface OntologySourceMapper {
    @Delete("delete from ontology_source where wsdsid=#{wsdsid}")
    boolean delete(@Param("wsdsid") int wsdsid);

    @Insert({"<script>",
            "INSERT INTO ontology_source (wsdsid, `match`, multimatch_ptids)",
            "VALUES ",
            "<foreach collection='ontologySources' item='ontologySource' separator=','>",
            "(#{ontologySource.wsdsid}, #{ontologySource.match}, #{ontologySource.multimatchPtids})",
            "</foreach>",
            "</script>"
    })
    int insertList(@Param("ontologySources") List<OntologySourcePO> ontologySources);
}
