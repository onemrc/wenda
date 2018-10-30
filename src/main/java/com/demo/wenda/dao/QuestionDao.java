package com.demo.wenda.dao;

import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionDao {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = "title,content,user_id,anonymous,create_time,tag_id";
    String SELECT_FIELDS = "question_id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{title},#{content},#{userId},#{anonymous},#{createTime},#{tagId})"})
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                             @Param("limit") int limit);
}
