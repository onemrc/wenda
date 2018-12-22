package com.demo.wenda.async.handler;

import com.demo.wenda.async.EventHandler;
import com.demo.wenda.async.EventModel;
import com.demo.wenda.domain.Message;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.enums.ReadStatus;
import com.demo.wenda.service.MessageService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * create by: one
 * create time:2018/12/20 22:10
 * 描述：关注事件
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doEventHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreateDate(new Date());
        message.setHasRead(ReadStatus.NOT_READ.getCode());
        message.setConversationId();

        User user = userService.getById(eventModel.getActorId());
        if (eventModel.getEntityType() == EntityType.QUESTION.getValue()) {
            message.setContent("用户: " + user.getName()
                    + "关注了你的问题,http://127.0.0.1:8080/question/" + eventModel.getEntityId());
        } else if (eventModel.getEntityType() == EntityType.ENTITY_USER.getValue()) {
            message.setContent("用户: " + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + eventModel.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
