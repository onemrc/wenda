package com.demo.wenda.controller;

import com.demo.wenda.domain.Question;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.SensitiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    SensitiveService sensitiveService;


    public Question addQuestion(Question question){
        //html标签过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

         return question;
    }

//    @RequestMapping(value = "/add")
//    public String addQuestion(){
//
//    }
}
