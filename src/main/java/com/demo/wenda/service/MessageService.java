package com.demo.wenda.service;

import com.demo.wenda.dao.MessageDAO;
import com.demo.wenda.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private MessageDAO messageDAO;

    private SensitiveService sensitiveService;

    @Autowired
    public MessageService(MessageDAO messageDAO, SensitiveService sensitiveService) {
        this.messageDAO = messageDAO;
        this.sensitiveService = sensitiveService;
    }

    public int addMessage(Message message) {
        // 对私信内容进行敏感词过滤
        message.setContent(sensitiveService.filter(message.getContent()));

        return messageDAO.addMessage(message);
    }


    public List<Message> getConversationList(int userId,int offset, int limit ){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public Integer getConversationUnReadCount(Integer userId, String conversationId) {
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }

    public int getConversationCount(String conversationId){
        return messageDAO.getConversationCount(conversationId);
    }
}
