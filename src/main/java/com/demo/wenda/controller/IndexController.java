package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import com.demo.wenda.dto.HostUserDTO;
import com.demo.wenda.dto.QuestionDTO;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.*;
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


import java.util.*;

/**
 * 问题展示页
 */
@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final QuestionService questionService;

    private final UserService userService;

    private final CommentService commentService;

    private final TagService tagService;

    private final HostHolder hostHolder;

    private final FollowService followService;

    private final CollectionService collectionService;


    @Autowired
    public IndexController(QuestionService questionService, UserService userService, CommentService commentService, TagService tagService, HostHolder hostHolder, FollowService followService, CollectionService collectionService) {
        this.questionService = questionService;
        this.userService = userService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.hostHolder = hostHolder;
        this.followService = followService;
        this.collectionService = collectionService;
    }

    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        List<Question> questionList = questionService.getLatestQuestions();

        List<ViewObject> vos = new ArrayList<>();


        //问题数据封装
        for (Question question : questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getById(question.getUserId()));
//            vo.set("tagName",tagService.getNameById(question.getTagId()));

            vo.set("lookCount",questionService.getLookCount(question.getQuestionId()));

            //获取tags
            Set<String> tags = questionService.getTags(question.getQuestionId());
            List<String> tagNameList = new ArrayList<>();
            for (String tag:tags){
                tagNameList.add(tagService.getNameById(Integer.valueOf(tag)));
            }
            vo.set("tagNameList",tagNameList);

            vos.add(vo);
        }

        //个人消息数据封装
        model.addAttribute("isLogin",false);
        if (hostHolder.getUsers()!= null){
            User hostUser = hostHolder.getUsers();

            HostUserDTO hostUserDTO = new HostUserDTO();
            hostUserDTO.setUserName(hostUser.getName());
            hostUserDTO.setIntroduction(userService.getIntroductionById(hostUser.getUserId()));
            hostUserDTO.setFollowerCount(followService.getFollowerCount(EntityType.ENTITY_USER.getValue(),hostUser.getUserId()));
            hostUserDTO.setCollectionCount(collectionService.getUserCollectionCount(hostUser.getUserId()));
            hostUserDTO.setCommentCount(commentService.getUserAnswerCount(hostUser.getUserId()));
            model.addAttribute("hostUser",hostUserDTO);
            model.addAttribute("isLogin",true);
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
