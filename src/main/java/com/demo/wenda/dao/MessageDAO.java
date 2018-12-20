package com.demo.wenda.dao;

import com.demo.wenda.domain.Message;
import com.demo.wenda.domain.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id, to_id, content, has_read, conversation_id, create_date";
    String SELECT_FIELDS = "message_id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{fromId}, #{toId}, #{content}, #{hasRead}, #{conversationId}, #{createDate})"})
    int addMessage(Message message);

    @Select({"select ", INSERT_FIELDS, " , count(message_id) as id from ( select * from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by create_date desc) tt group by conversation_id order by create_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);


    @Select({"select count(message_id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
