package com.demo.wenda.controller;

import com.alibaba.druid.util.StringUtils;
import com.demo.wenda.domain.*;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.*;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.utils.RedisKeyUtil;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题
 */
@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;

    private HostHolder hostHolder;

    private final CommentService commentService;

    private final TagService tagService;

    private final LikeService likeService;

    private final RedisService redisService;

    @Autowired
    public QuestionController(QuestionService questionService, HostHolder hostHolder, CommentService commentService, TagService tagService, LikeService likeService, RedisService redisService) {
        this.questionService = questionService;
        this.hostHolder = hostHolder;
        this.commentService = commentService;
        this.tagService = tagService;
        this.likeService = likeService;
        this.redisService = redisService;
    }

    /**
     * 发布问题
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestParam("title") String title,
                      @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);

            questionService.addQuestion(question);

            return ConverterUtil.getJSONString(0, "问题发布成功");
        } catch (Exception e) {
            logger.error("发布问题异常：{}", e.getMessage());
            return ConverterUtil.getJSONString(1, "问题发布失败");
        }
    }

    @RequestMapping(value = {"/addQuestion"}, method = RequestMethod.POST)
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
    @RequestMapping(value = "/{questionId}", method = RequestMethod.GET)
    public String questionDetail(Model model,
                                 @PathVariable("questionId") Integer questionId) {
        List<ViewObject> vos = new ArrayList<>();

        Question question = questionService.getById(questionId);

        model.addAttribute("question",question);

        if (question.getTagId()!= null){
            String tagName = tagService.getNameById(question.getTagId());
            model.addAttribute("tagName",tagName);
        }


        //该问题下的回答
        List<Comment> AnswerList = commentService.getCommentByEntity(questionId, EntityType.ENTITY_ANSWER.getValue());
        for (Comment answer:AnswerList){
            ViewObject vo = new ViewObject();
            vo.set("answer",answer);

            //这个回答下有多少评论
            int commentCount= commentService.getCommentCount(answer.getEntityId(),EntityType.ENTITY_COMMENT.getValue());
            vo.set("commentCount",commentCount);

            //这个回答有多少个赞
            vo.set("likeCount",likeService.getCommentLikeCount(EntityType.ENTITY_ANSWER.getValue(),answer.getCommentId()));

            vos.add(vo);
        }

        return "detail";
    }



}
