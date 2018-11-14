package com.demo.wenda.dao;

import com.demo.wenda.domain.Message;
import com.demo.wenda.domain.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id, to_id, content, has_read, conversation_id, create_date";
    String SELECT_FIELDS = "message_id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{fromId}, #{toId}, #{content}, #{hasRead}, #{conversationId}, #{createDate})"})
    int addMessage(Message message);




}
