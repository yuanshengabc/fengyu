package cn.deepclue.datamaster.cleaner.dao.cleaning.mapper;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * Created by magneto on 17-4-8.
 */
@Mapper
public interface TaskMapper {

    @Select("SELECT * FROM `task` limit #{offset}, #{limit}")
    List<Task> getAllTasks(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT * FROM `task` where status = #{status} limit #{offset}, #{limit}")
    List<Task> getTasks(@Param("offset") int offset, @Param("limit") int limit, @Param("status") int status);

    @Select("SELECT * FROM `task` WHERE tid = #{tid}")
    Task getTask(@Param("tid") int tid);

    @Insert("INSERT INTO `task` (status, source_id, name, sink_id, type, created_on, modified_on, extra_data) VALUES " +
            "(#{task.status}, #{task.sourceId}, #{task.name}, #{task.sinkId}, #{task.type}, " +
            "#{task.createdOn}, #{task.modifiedOn}, #{task.extraData})")
    @SelectKey(
            resultType = Integer.class,
            statement = "SELECT LAST_INSERT_ID() AS tid",
            statementType = StatementType.STATEMENT,
            before = false,
            keyProperty = "task.tid",
            keyColumn = "tid"
    )
    boolean insertTask(@Param("task") Task task);

    @Update("UPDATE `task` SET status = #{task.status}, name = #{task.name}, modified_on = #{task.modifiedOn}, extra_data = #{task.extraData} WHERE tid = #{task.tid}")
    boolean update(@Param("task") Task task);
}
