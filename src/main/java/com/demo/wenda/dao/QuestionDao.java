package com.demo.wenda.dao;

import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDao {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = "title,content,user_id,anonymous,create_time,tag_id,look_count,comment_count";
    String SELECT_FIELDS = "question_id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{title},#{content},#{userId},#{anonymous},#{createTime},#{tagId},#{lookCount},#{commentCount})"})
    @Options(useGeneratedKeys=true, keyProperty="questionId", keyColumn="question_id")//插入成功后马上返回，其自增的id
    int addQuestion(Question question);


    List<Question> selectUserLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                             @Param("limit") int limit);

    @Select({"select * from "+TABLE_NAME+" where question_id = #{id}"})
    Question getById(int id);

    @Select({"select * from "+TABLE_NAME+" order by create_time desc"})
    List<Question> selectLatestQuestions();


}
