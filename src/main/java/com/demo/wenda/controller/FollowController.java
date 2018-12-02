package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.FollowService;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 关注
 */
@Controller
public class FollowController {

    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    /**
     * 当前用户关注另一个用户
     * @param userId 被关注者的id
     * @return
     */
    @PostMapping(value = {"/followUser"})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {

        //当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        //当前用户关注一个人
        boolean res = followService.follow(hostUser.getUserId(), userId, EntityType.ENTITY_USER.getValue());


        Map<String, Object> message = new HashMap<>();

        //粉丝数
        message.put("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER.getValue(), userId));

        //关注数
        message.put("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER.getValue(), userId));

        return ConverterUtil.getJSONString(res ? 0 : 1, message);
    }
}
