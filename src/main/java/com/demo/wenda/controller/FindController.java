package com.demo.wenda.controller;

import com.demo.wenda.domain.Find;
import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.FindService;
import com.demo.wenda.service.FollowService;
import com.demo.wenda.service.RedisService;
import com.demo.wenda.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * create by: one
 * create time:2018/12/22 16:46
 * 描述：发现入口
 */
@Controller
public class FindController {

    private static final Logger logger = LoggerFactory.getLogger(FindController.class);

    @Autowired
    FindService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    RedisService redisService;

    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPushFeeds(Model model) {

        if (hostHolder.getUsers()!=null){
            model.addAttribute("localUser",hostHolder.getUsers());
        }

        int localUserId = hostHolder.getUsers() != null ? hostHolder.getUsers().getUserId() : 0;


        List<String> feedIds = redisService.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Find> finds = new ArrayList<Find>();
        for (String feedId : feedIds) {
            Find feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                finds.add(feed);
            }
        }
        model.addAttribute("finds", finds);
        return "finds";
    }

    /**
     *
     *  拉模式
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullFeeds(Model model) {
        if (hostHolder.getUsers()!=null){
            model.addAttribute("localUser",hostHolder.getUsers());
        }

        int localUserId = hostHolder.getUsers() != null ? hostHolder.getUsers().getUserId() : 0;
        Set<String> followees = new HashSet<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followService.getFollowees(EntityType.ENTITY_USER.getValue(),localUserId,0, 10000);
        }
        List<Find> finds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("finds", finds);
        return "finds";
    }
}
