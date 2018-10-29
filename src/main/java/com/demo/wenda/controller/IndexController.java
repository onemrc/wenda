package com.demo.wenda.controller;

import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import com.demo.wenda.dto.QuestionDTO;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/wenda")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/index")
    public String index(Model model){
        List<Question> questionList = questionService.getLatestQuestions(1,0,10);

        List<QuestionDTO> vos = new ArrayList<>();

        //数据封装
        for (Question question : questionList){
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setTitle(question.getTitle());
            questionDTO.setAnswerCount(question.getAnswerCount());

            User user = userService.getById(question.getUserId());
            questionDTO.setUserName(user.getName());

            questionDTO.setContent(question.getContent());
            questionDTO.setCreateTime(question.getCreatedTime());

            vos.add(questionDTO);
        }

        model.addAttribute("vos",vos);

        return "index";
    }

    @RequestMapping(value = "/home")
    public String Hello(){
        return "home";
    }


}
