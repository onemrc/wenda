package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
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

    /**
     * 当前用户给某个回答点赞
     * @param commentId 评论（回答）id
     * @return
     */
    @PostMapping(value = {"/addLikeToAnswer"},params = {"commentId"})
    @ResponseBody
    public String addLikeToAnswer(@PathParam("commentId") int commentId){
        //获取当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        //点赞
        boolean res = likeService.commentLike(hostUser.getUserId(),commentId, EntityType.ENTITY_ANSWER.getValue());

        //成功返回0 ，失败返回1
        return ConverterUtil.getJSONString(res? 0 :1);
    }

    /**
     * 当前用户取消对某个回答的赞
     * @param commentId
     * @return
     */
    @PostMapping(value = {"/cancelLikeToAnswer"},params = {"commentId"})
    @ResponseBody
    public String cancelLikeToAnswer(@PathParam("commentId") int commentId){
        //获取当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        boolean res = likeService.cancelLikeToAswer(hostUser.getUserId(),EntityType.ENTITY_ANSWER.getValue(),commentId);

        //成功返回0 ，失败返回1
        return ConverterUtil.getJSONString(res? 0 :1);
    }
}
