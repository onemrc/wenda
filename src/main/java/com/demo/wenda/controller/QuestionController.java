package com.demo.wenda.controller;

import com.demo.wenda.domain.Comment;
import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Question;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.CommentService;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private QuestionService questionService;

    private HostHolder hostHolder;

    private final CommentService commentService;

    @Autowired
    public QuestionController(QuestionService questionService, HostHolder hostHolder, CommentService commentService) {
        this.questionService = questionService;
        this.hostHolder = hostHolder;
        this.commentService = commentService;
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

        //该问题下的回答
        List<Comment> AnswerList = commentService.getCommentByEntity(questionId, EntityType.ENTITY_ANSWER.getValue());
        for (Comment answer:AnswerList){
            ViewObject vo = new ViewObject();
            vo.set("answer",answer);
            vos.add(vo);
        }

        return "detail";
    }



}
