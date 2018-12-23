package com.demo.wenda.async.handler;

import com.demo.wenda.async.EventHandler;
import com.demo.wenda.async.EventModel;
import com.demo.wenda.domain.Message;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.enums.ReadStatus;
import com.demo.wenda.service.CommentService;
import com.demo.wenda.service.MessageService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.utils.WendaUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * create by: one
 * create time:2018/12/22 23:12
 * 描述：评论
 */
@Component
public class CommentHandler implements EventHandler {

    private final CommentService commentService;
    private final MessageService messageService;
    private final UserService userService;

    public CommentHandler(CommentService commentService, MessageService messageService, UserService userService) {
        this.commentService = commentService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void doEventHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setConversationId();
        message.setHasRead(ReadStatus.NOT_READ.getCode());
        message.setCreateDate(new Date());

        String actorName = userService.getUserNameById(eventModel.getActorId());

        if (eventModel.getEntityType() == EntityType.QUESTION.getValue()){
            message.setContent("用户: "+actorName+" 回答了你的问题，<a href='/question/"+eventModel.getExt("questionId")+"'>http://127.0.0.1:8080/question/"+eventModel.getEntityId() + "</a>");
        }else if (eventModel.getEntityType() == EntityType.ENTITY_ANSWER.getValue()){
            message.setContent("用户: "+actorName+" 评论了你的回答，<a href='/question/"+eventModel.getExt("questionId")+"'>http://127.0.0.1:8080/question/"+eventModel.getEntityId() + "</a>");
        } else if (eventModel.getEntityType() == EntityType.ENTITY_COMMENT.getValue()) {
            message.setContent("用户: "+actorName+" 回复了你的评论，<a href='/question/"+eventModel.getExt("questionId")+"'>http://127.0.0.1:8080/question/"+eventModel.getEntityId() + "</a>");
        }


        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}
