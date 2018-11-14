package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Question;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private QuestionService questionService;

    private HostHolder hostHolder;

    @Autowired
    public QuestionController(QuestionService questionService, HostHolder hostHolder) {
        this.questionService = questionService;
        this.hostHolder = hostHolder;
    }

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

    @RequestMapping(value = "/{questionId}", method = RequestMethod.GET)
    public String questionDetail(Model model,
                                 @PathVariable("questionId") int questionId) {
        Question question = questionService.getById(questionId);
        model.addAttribute("question",question);

        return "detail";
    }


}
