package com.demo.wenda.dao;

import com.demo.wenda.domain.Comment;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "user_id,entity_id,entity_type,content,create_date,comment_status";


    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{userId},#{entityId},#{entityType},#{content},#{createDate},#{commentStatus})"})
    int addComment(Comment comment);

    @Select({"select * from",TABLE_NAME,"where comment_id = #{commentId}"})
    Comment getCommentById(Integer commentId);

    @Select({"select * from",TABLE_NAME,"where entity_id = #{entityId} and entity_type = #{entityType} order by create_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);


    @Select({"select count(comment_id) from",TABLE_NAME,"where entity_id = #{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);



    @Update({"update set comment_status={statusCode}",TABLE_NAME," where entity_id = #{entityId} and entityType = #{entityType} and user_id=#{userId}"})
    int deleteComment(@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("userId") int userId,@Param("statusCode") int statusCode);

    @Select({"select comment_id from",TABLE_NAME,"where entity_id = #{entityId} and entity_type = #{entityType} and user_id = #{userId}"})
    Integer selectIdByEntityIdAndEntityType(@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("userId") int userId);


    @Select({"select entity_id from",TABLE_NAME,"where comment_id = #{commentId}"})
    Integer getEntityId(int commentId);

    @Select({"select count(*) from",TABLE_NAME,"where user_id = #{userId} and entity_type=3"})
    Long getUserAnswerCount(int userId);

}
