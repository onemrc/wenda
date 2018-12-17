package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import com.demo.wenda.dto.FollowerDTO;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.*;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

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

    /**
     * 当前用户一个人都没有关注，为其推荐
     * @return
     */
    @GetMapping(value = {"/recommendFollow"})
    @ResponseBody
    public String recommendFollow(@RequestParam(value = "currIndex", defaultValue = "0") Integer currIndex,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        List<FollowerDTO> followerDTOList =  new LinkedList<>();

        //从数据库查10个人出来
        List<Integer> userIdList = userService.getUserIdBySize(currIndex,pageSize);
        for (Integer userId: userIdList){//封装数据
            FollowerDTO followerDTO = new FollowerDTO();
            followerDTO.setName(userService.getUserNameById(userId));
            followerDTO.setLikeCount(likeService.getUserLikeCount(userId,EntityType.ENTITY_ANSWER.getValue()));
            followerDTO.setAnswerCount(commentService.getUserAnswerCount(userId));
            followerDTO.setQuestionCount(questionService.getUserQuestinCount(userId));
            followerDTO.setIntroduction(userService.getIntroductionById(userId));
            followerDTO.setHeadUrl(userService.getUserHeadUrl(userId));
            followerDTO.setFolloweeCount(followService.getFollowerCount(EntityType.ENTITY_USER.getValue(),userId));
            followerDTO.setUserId(userId);

            followerDTOList.add(followerDTO);
        }

       return ConverterUtil.getJSONString(followerDTOList);
    }
}
