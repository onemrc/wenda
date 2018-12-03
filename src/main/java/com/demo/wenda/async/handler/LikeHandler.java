package com.demo.wenda.async.handler;

import com.demo.wenda.async.EventHandler;
import com.demo.wenda.async.EventModel;
import com.demo.wenda.domain.Message;
import com.demo.wenda.domain.User;
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
 * create time:2018/12/2 22:55
 * 描述：点赞事件
 */
@Component
public class LikeHandler implements EventHandler {

    private final MessageService messageService;

    private final UserService userService;

    @Autowired
    public LikeHandler(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void doEventHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreateDate(new Date());
        String actorUserName = userService.getUserNameById(eventModel.getActorId());
        message.setContent("用户:"+actorUserName+" 赞了你的评论，http://127.0.0.1:8080/wenda/question/"+eventModel.getExt("questionId"));

        message.setHasRead(ReadStatus.NOT_READ.getCode());
        message.setConversationId();
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
