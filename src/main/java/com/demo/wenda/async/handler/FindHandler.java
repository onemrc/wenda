package com.demo.wenda.async.handler;

import com.demo.wenda.async.EventHandler;
import com.demo.wenda.async.EventModel;
import com.demo.wenda.domain.Find;
import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.service.*;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * create by: one
 * create time:2018/12/22 18:30
 * 描述：发现
 */
@Component
public class FindHandler implements EventHandler {

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    FindService findService;

    @Autowired
    RedisService redisService;

    @Autowired
    QuestionService questionService;

    /**
     * 将EventModel 中的数据拿出来
     * @param model
     * @return
     */
    private String buildFindData(EventModel model) {
        Map<String, String> map = new HashMap<String ,String>();
        // 触发用户是通用的
        User actor = userService.getById(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getUserId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getEventType() == EventType.COMMENT ||
                (model.getEventType() == EventType.FOLLOW  && model.getEntityType() == EntityType.QUESTION.getValue())) {
            Question question = questionService.getById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getQuestionId()));
            map.put("questionTitle", question.getTitle());
            return ConverterUtil.getJSONString(map);
        }
        return null;
}

    @Override
    public void doEventHandle(EventModel eventModel) {


        // 构造一个新鲜事
        Find find = new Find();
        find.setCreateDate(new Date());
        find.setType(eventModel.getEventType().getValue());
        find.setUserId(eventModel.getActorId());
        find.setData(buildFindData(eventModel));
        if (find.getData() == null) {// 不支持的find
            return;
        }
        findService.addFeed(find);

        // 获得所有粉丝
        Set<String>  followersSet = followService.getFollowers(EntityType.ENTITY_USER.getValue(), eventModel.getActorId(), 0,Integer.MAX_VALUE);
        List<Integer> followers =new ArrayList<>();
        for (String follower:followersSet){
            followers.add(Integer.valueOf(follower));
        }

        // 系统队列
        followers.add(0);
        // 给所有粉丝推事件
        for (int follower : followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            redisService.lpush(timelineKey, String.valueOf(find.getId()));
            // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
