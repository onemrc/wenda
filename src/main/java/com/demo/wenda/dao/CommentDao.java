package com.demo.wenda.dao;

import com.demo.wenda.domain.Comment;
import com.demo.wenda.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentDao {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "user_id,entity_id,entity_type,content,create_date,comment_status";


    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{userId},#{entityId},#{entityType},#{content},#{createDate},#{commentStatus})"})
    int addComment(Comment comment);

    @Select({"select * from",TABLE_NAME,"where comment_id = #{commentId}"})
    User getCommentById(Integer commentId);

    @Select({"select * from",TABLE_NAME,"where entity_id = #{entityId} and entityType = #{entityType} order by create_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);


    @Select({"select count(comment_id) from",TABLE_NAME,"where entity_id = #{entityId} and entityType = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);
}
