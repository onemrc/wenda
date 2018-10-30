package com.demo.wenda.controller;

import com.demo.wenda.domain.Question;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        List<Question> questionList = questionService.getLatestQuestions(1,0,10);

        List<ViewObject> vos = new ArrayList<>();


        //数据封装
        for (Question question : questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getById(question.getUserId()));

            vos.add(vo);
        }

        model.addAttribute("vos",vos);

        return "index";
    }

    @RequestMapping(value = "/home")
    public String Hello(){
        return "home";
    }


}
