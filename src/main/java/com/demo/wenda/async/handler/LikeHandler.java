package com.demo.wenda.async.handler;

import com.demo.wenda.async.EventHandler;
import com.demo.wenda.async.EventModel;
import com.demo.wenda.domain.Message;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.enums.ReadStatus;
import com.demo.wenda.service.MessageService;
import com.demo.wenda.service.RedisService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.utils.RedisKeyUtil;
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

    private final RedisService redisService;

    @Autowired
    public LikeHandler(MessageService messageService, UserService userService, RedisService redisService) {
        this.messageService = messageService;
        this.userService = userService;
        this.redisService = redisService;
    }

    @Override
    public void doEventHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreateDate(new Date());
        String actorUserName = userService.getUserNameById(eventModel.getActorId());
        message.setContent("用户: "+actorUserName+" 赞了你的评论，<a href='/question/"+eventModel.getExt("questionId")+"'>http://127.0.0.1:8080/question/"+eventModel.getExt("questionId") + "</a>");

        message.setHasRead(ReadStatus.NOT_READ.getCode());
        message.setConversationId();
        messageService.addMessage(message);

        //被点赞数+1
        String key = RedisKeyUtil.getUserLikecount(eventModel.getEntityOwnerId());

        //该问题下总点赞数+1
        redisService.zincrby(RedisKeyUtil.getQuestionLikecount(), 1, eventModel.getExt("questionId"));
        redisService.incr(key);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
