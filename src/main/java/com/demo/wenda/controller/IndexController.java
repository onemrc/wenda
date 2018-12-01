package com.demo.wenda.controller;

import com.demo.wenda.domain.Question;
import com.demo.wenda.dto.QuestionDTO;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.CommentService;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.utils.ResultVoUtil;
import com.demo.wenda.vo.ResultVo;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final QuestionService questionService;

    private final UserService userService;

    private final CommentService commentService;

    @Autowired
    public IndexController(QuestionService questionService, UserService userService, CommentService commentService) {
        this.questionService = questionService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        List<Question> questionList = questionService.getUserLatestQuestions(1,0,10);

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

//    @GetMapping(value = {"/new"})
//    public String LatestQuestion(Model model){
//        List<Question> questionList = questionService.getLatestQuestions();
//
//        List<ViewObject> vos = new ArrayList<>();
//
//        //数据封装
//        for (Question question : questionList){
//            ViewObject vo = new ViewObject();
//            vo.set("question",question);
//
//
//            vos.add(vo);
//        }
//        model.addAttribute("vos",vos);
//
//        return "questionList";
//    }

    @GetMapping(value = {"/new"})
    @ResponseBody
    public ResultVo LatestQuestion(){
        List<Question> questionList = questionService.getLatestQuestions();

        List<QuestionDTO> vos = new ArrayList<>();

//        Map<String,Object> vos = new HashMap<>();

        //数据封装
        for (Question question : questionList){
            QuestionDTO vo = new QuestionDTO();
            BeanUtils.copyProperties(question,vo);
            vo.setUserName(userService.getUserNameById(question.getUserId()));
            vo.setAnswerCount(commentService.getCommentCount(question.getQuestionId(), EntityType.ENTITY_QUESTION.getValue()));
            vo.setCreateTime(ConverterUtil.dateToString(question.getCreateTime()));

            vos.add(vo);
//            vos.put("question",question);
        }
//        String data = ConverterUtil.getJSONString(vos);

        return ResultVoUtil.success(vos);
    }

}
