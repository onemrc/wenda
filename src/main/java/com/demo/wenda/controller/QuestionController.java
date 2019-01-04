package com.demo.wenda.controller;

import com.alibaba.druid.util.StringUtils;
import com.demo.wenda.async.EventHandler;
import com.demo.wenda.async.EventModel;
import com.demo.wenda.async.EventProducer;
import com.demo.wenda.domain.*;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.service.*;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.utils.RedisKeyUtil;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 问题
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;

    private HostHolder hostHolder;

    private final CommentService commentService;

    private final TagService tagService;

    private final LikeService likeService;

    private final RedisService redisService;

    private final FollowService followService;

    private final UserService userService;

    private final ProofService proofService;

    private final EventProducer eventProducer;

    @Autowired
    public QuestionController(QuestionService questionService, HostHolder hostHolder, CommentService commentService, TagService tagService, LikeService likeService, RedisService redisService, FollowService followService, UserService userService, ProofService proofService, EventProducer eventProducer) {
        this.questionService = questionService;
        this.hostHolder = hostHolder;
        this.commentService = commentService;
        this.tagService = tagService;
        this.likeService = likeService;
        this.redisService = redisService;
        this.followService = followService;
        this.userService = userService;
        this.proofService = proofService;
        this.eventProducer = eventProducer;
    }

    /**
     * 发布问题
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestParam("title") String title,
                      @RequestParam("content") String content) {
        try {
            if (hostHolder.getUsers() == null){
                return ConverterUtil.getJSONString(999);
            }

            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);

            question.setUserId(hostHolder.getUsers().getUserId());
            question.setCommentCount(0);

            questionService.addQuestion(question);

            return ConverterUtil.getJSONString(0, "问题发布成功");
        } catch (Exception e) {
            logger.error("发布问题异常：{}", e.getMessage());
            return ConverterUtil.getJSONString(1, "问题发布失败");
        }
    }

    @RequestMapping(value = {"/question/addQuestion"}, method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                              @RequestParam(value = "tag_name",defaultValue = "") String tagName,
                              @RequestParam(value = "anonymous",defaultValue = "1") int anonymous){
        User hostUser = hostHolder.getUsers();
        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }



        Question question = new Question();


        question.setTitle(title);
        question.setContent(content);

        question.setUserId(hostUser.getUserId());

        question.setAnonymous(anonymous);

//        question.setLookCount(0);
        question.setCommentCount(0);

        //存进数据库，顺便拿到新的id
        Integer newQuestionId =  questionService.addQuestion(question);

        if (!StringUtils.isEmpty(tagName)){
            String[] tags =  tagName.split(" ");
            for (String tag:tags){
                if (tagService.getIdByName(tag) == null){//没有这个标签
                    //存进数据库
                    Integer newTagId = tagService.addTag(tag);

                    //存进redis
                    questionService.addTag(newQuestionId,newTagId);

                    tagService.addQuestion(newQuestionId,newTagId);
                }else {
                    Integer TagId = tagService.getIdByName(tag);
                    //存进redis
                    questionService.addTag(newQuestionId,TagId);

                    tagService.addQuestion(newQuestionId,TagId);
                }
            }
        }
        //初始化浏览数
        redisService.set(RedisKeyUtil.getQuestionLook(newQuestionId),0+"");


        return ConverterUtil.getJSONString(200,"发布成功");
    }



    /**
     * 问题详情
     * @param model
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.GET)
    public String questionDetail(Model model,
                                 @PathVariable("questionId") Integer questionId) {

        Question question = questionService.getById(questionId);

        model.addAttribute("question",question);

//        if (question.getTagId()!= null){
//            String tagName = tagService.getNameById(question.getTagId());
//            model.addAttribute("tagName",tagName);
//        }

        model.addAttribute("followed",false);
        if (hostHolder.getUsers() != null){
            model.addAttribute("localUser",hostHolder.getUsers());
            //当前用户是否已关注该问题
            model.addAttribute("followed",followService.isFollow(EntityType.QUESTION.getValue(),questionId,hostHolder.getUsers().getUserId()));
        }

        //这个问题的关注人人数
        Long followUserCount = followService.getFollowerCount(EntityType.QUESTION.getValue(),questionId);
        model.addAttribute("followUserCount",followUserCount);

        //这个问题关注的人,太多的话取10个算了
        Set<String> users = followService.getFollowers(EntityType.QUESTION.getValue(),questionId,0,100);
        List<ViewObject> followUsers = new ArrayList<>();
        for (String userId:users){
            ViewObject follower = new ViewObject();
            Integer id = Integer.valueOf(userId);
            follower.set("id",id);
            follower.set("name",userService.getUserNameById(id));
            follower.set("headUrl",userService.getUserHeadUrl(id));
            followUsers.add(follower);
        }
        model.addAttribute("followUsers",followUsers);

        //该问题下的回答
        List<Comment> AnswerList = commentService.getCommentByEntity(questionId, EntityType.QUESTION.getValue());
        List<ViewObject> comments = new ArrayList<>();
        for (Comment answer:AnswerList){
            ViewObject comment = new ViewObject();

            //这个回答的用户名
            comment.set("userName",userService.getUserNameById(answer.getUserId()));

            comment.set("userHeadUrl",userService.getUserHeadUrl(answer.getUserId()));

            comment.set("commentId",answer.getCommentId());

//            comment.set("entityType",commentService.getEntityTypeById(answer.getCommentId()));

            //这个回答的内容
            comment.set("content",commentService.getContentById(answer.getCommentId()));

            //这个回答下有多少评论
            int commentCount= commentService.getCommentCount(answer.getEntityId(),EntityType.ENTITY_ANSWER.getValue());
            comment.set("commentCount",commentCount);

            //这个回答有多少个赞
            comment.set("likeCount",likeService.getCommentLikeCount(EntityType.ENTITY_COMMENT.getValue(),answer.getCommentId()));

            //当前用户是否给这个回答点过赞
            comment.set("liked",false);
//            //当前用户是否关注了这个回答
//            comment.set("followed",false);
            if (hostHolder.getUsers() != null){
                comment.set("liked",likeService.cancelLikeToAswer(hostHolder.getUsers().getUserId(),EntityType.ENTITY_ANSWER.getValue(),answer.getCommentId()));
                comment.set("followed",followService.follow(hostHolder.getUsers().getUserId(),answer.getCommentId(),EntityType.ENTITY_ANSWER.getValue()));
            }
            comment.set("proofName", proofService.getProofByUserId(answer.getUserId()).getTypeName());

            comments.add(comment);
        }
        model.addAttribute("comments",comments);


        return "detail";
    }



}
