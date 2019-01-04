package com.demo.wenda.controller;

import com.demo.wenda.async.EventModel;
import com.demo.wenda.async.EventProducer;
import com.demo.wenda.domain.Comment;
import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.service.CommentService;
import com.demo.wenda.service.LikeService;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;

/**
 * create by: one
 * create time:2018/12/1 13:55
 * 描述：点赞
 */
@Controller
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    /**
     * 当前用户给某个评论点赞
     * @param commentId 评论（回答）id
     * @return
     */
    @PostMapping(value = {"/like"},params = {"commentId"})
    @ResponseBody
    public String addLikeToAnswer(@PathParam("commentId") int commentId){
        //获取当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        Comment comment = commentService.getCommentById(commentId);

        //异步发送一个私信给TA点赞的人,那个人被点赞数+1
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostUser.getUserId())
                .setEntityType(EntityType.ENTITY_COMMENT.getValue())
                .setEntityId(commentId)
                .setEntityOwnerId(comment.getUserId())
                .setExts("questionId",String.valueOf(comment.getEntityId())));

        //点赞
        Long likeCount = likeService.commentLike(hostUser.getUserId(),commentId, EntityType.ENTITY_COMMENT.getValue());


        //返回最新点赞人数
        return ConverterUtil.getJSONString(0,"likeCount",likeCount);
    }

    /**
     * 当前用户取消对某个评论的赞
     * @param commentId
     * @return
     */
    @PostMapping(value = {"/dislike"},params = {"commentId"})
    @ResponseBody
    public String cancelLikeToAnswer(@PathParam("commentId") int commentId){
        //获取当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        boolean res = likeService.cancelLikeToAswer(hostUser.getUserId(),EntityType.ENTITY_COMMENT.getValue(),commentId);

        //成功返回0 ，失败返回1
        return ConverterUtil.getJSONString(res? 0 :1);
    }
}
