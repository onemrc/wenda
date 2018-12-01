package com.demo.wenda.controller;

import com.alibaba.druid.util.StringUtils;
import com.demo.wenda.domain.*;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.CommentService;
import com.demo.wenda.service.LikeService;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.TagService;
import com.demo.wenda.utils.ConverterUtil;
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

@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;

    private HostHolder hostHolder;

    private final CommentService commentService;

    private final TagService tagService;

    private final LikeService likeService;

    @Autowired
    public QuestionController(QuestionService questionService, HostHolder hostHolder, CommentService commentService, TagService tagService, LikeService likeService) {
        this.questionService = questionService;
        this.hostHolder = hostHolder;
        this.commentService = commentService;
        this.tagService = tagService;
        this.likeService = likeService;
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


        if (!StringUtils.isEmpty(tagName)){

            if (tagService.getIdByName(tagName) == null){//没有这个标签
                Tag tag = new Tag();
                tag.setTag_name(tagName);
                question.setTagId(tagService.addTag(tagName));
            }else {
                question.setTagId(tagService.getIdByName(tagName));
            }
        }

        question.setTitle(title);
        question.setContent(content);

        question.setUserId(hostUser.getUserId());

        question.setAnonymous(anonymous);

        question.setLookCount(0);
        question.setCommentCount(0);

        questionService.addQuestion(question);


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
                                 @PathVariable("questionId") int questionId) {
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
