package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.User;
import com.demo.wenda.dto.FollowerDTO;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.*;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    /**
     * 当前用户取消对某问题的关注
     * @param questionId
     * @return
     */
    @PostMapping(value = {"/unfollowQuestion"})
    @ResponseBody
    public String unFollowQuestion(@RequestParam("questionId") int questionId,
                         HttpServletRequest request) {
        if (hostHolder.getUsers() == null){
            return ConverterUtil.getJSONString(999);
        }

        boolean res =  followService.unFollow(EntityType.ENTITY_QUESTION.getValue(),questionId,hostHolder.getUsers().getUserId());


        return ConverterUtil.getJSONString(res?0:1);
    }


    /**
     * 某用户的粉丝
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{userId}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("userId") int userId){
        //先找10个
        Set<String> followeeIds = followService.getFollowers(EntityType.ENTITY_USER.getValue(),userId,0,10);

        //转一下类型
        List<Integer> IfollowerIds = new ArrayList<>();
        for (String fid:followeeIds){
            IfollowerIds.add(Integer.valueOf(fid));
        }

        if (hostHolder.getUsers() != null) {
            model.addAttribute("localUser",hostHolder.getUsers());
            model.addAttribute("followees", getUsersInfo(hostHolder.getUsers().getUserId(), IfollowerIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, IfollowerIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER.getValue()));
        model.addAttribute("curUser", userService.getById(userId));

        return "followers";
    }

    /**
     * 某用户关注了谁
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{userId}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("userId") int userId){

        //先找10个
        Set<String> followeeIds = followService.getFollowees(EntityType.ENTITY_USER.getValue(),userId,0,10);
        //转一下类型
        List<Integer> IfolloweeIds = new ArrayList<>();
        for (String fid:followeeIds){
            IfolloweeIds.add(Integer.valueOf(fid));
        }

        if (hostHolder.getUsers() != null) {
            model.addAttribute("localUser",hostHolder.getUsers());
            model.addAttribute("followees", getUsersInfo(hostHolder.getUsers().getUserId(), IfolloweeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, IfolloweeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER.getValue(),userId ));
        model.addAttribute("curUser", userService.getById(userId));
        return "followees";
    }


    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getById(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            //该用户回答问题数
            vo.set("commentCount", commentService.getCommentCount(EntityType.ENTITY_QUESTION.getValue(),uid));

            //该用户关注别人的人数
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER.getValue(), uid));

            //该用户被多少人关注
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER.getValue()));

            //当前用户是否关注了这个人
            if (localUserId != 0) {
                vo.set("followed", followService.isFollow(EntityType.ENTITY_USER.getValue(), uid, localUserId));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
