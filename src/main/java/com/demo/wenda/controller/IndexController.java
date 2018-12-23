package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Proof;
import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import com.demo.wenda.dto.QuestionDTO;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.ProofType;
import com.demo.wenda.service.*;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.utils.ResultVoUtil;
import com.demo.wenda.utils.ValidatorUtil;
import com.demo.wenda.vo.ResultVo;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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

    private final ProofService proofService;

    @Autowired
    public IndexController(QuestionService questionService, UserService userService, CommentService commentService, TagService tagService, HostHolder hostHolder, FollowService followService, CollectionService collectionService, ProofService proofService) {
        this.questionService = questionService;
        this.userService = userService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.hostHolder = hostHolder;
        this.followService = followService;
        this.collectionService = collectionService;
        this.proofService = proofService;
    }


    //    @GetMapping(value = {"/","/index"})
//    public String index(Model model){
//        List<Question> questionList = questionService.getLatestQuestions();
//
//        List<ViewObject> vos = new ArrayList<>();
//
//
//        //问题数据封装
//        for (Question question : questionList){
//            ViewObject vo = new ViewObject();
//            vo.set("question",question);
//            vo.set("user",userService.getById(question.getUserId()));
////            vo.set("tagName",tagService.getNameById(question.getTagId()));
//
//            vo.set("lookCount",questionService.getLookCount(question.getQuestionId()));
//
//            //获取tags
//            Set<String> tags = questionService.getTags(question.getQuestionId());
//            List<String> tagNameList = new ArrayList<>();
//            for (String tag:tags){
//                tagNameList.add(tagService.getNameById(Integer.valueOf(tag)));
//            }
//            vo.set("tagNameList",tagNameList);
//
//            vos.add(vo);
//        }
//
//        //个人消息数据封装
//        model.addAttribute("isLogin",false);
//        if (hostHolder.getUsers()!= null){
//            User hostUser = hostHolder.getUsers();
//
//            HostUserDTO hostUserDTO = new HostUserDTO();
//            hostUserDTO.setUserName(hostUser.getName());
//            hostUserDTO.setIntroduction(userService.getIntroductionById(hostUser.getUserId()));
//            hostUserDTO.setFollowerCount(followService.getFollowerCount(EntityType.ENTITY_USER.getValue(),hostUser.getUserId()));
//            hostUserDTO.setCollectionCount(collectionService.getUserCollectionCount(hostUser.getUserId()));
//            hostUserDTO.setCommentCount(commentService.getUserAnswerCount(hostUser.getUserId()));
//            model.addAttribute("hostUser",hostUserDTO);
//            model.addAttribute("isLogin",true);
//        }
//
//
//        model.addAttribute("vos",vos);
//
//        return "index";
//    }

    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        List<Question> questionList = questionService.getLatestQuestions();
        List<ViewObject> vos = new ArrayList<>();
        for (Question question:questionList){
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followService.getFollowerCount(EntityType.QUESTION.getValue(), question.getQuestionId()));
            vo.set("user", userService.getById(question.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("localUser",hostHolder.getUsers());
        model.addAttribute("vos", vos);
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
            vo.setAnswerCount(commentService.getCommentCount(question.getQuestionId(), EntityType.QUESTION.getValue()));
            vo.setCreateTime(ConverterUtil.dateToString(question.getCreateTime()));

            vos.add(vo);
//            vos.put("question",question);
        }
//        String data = ConverterUtil.getJSONString(vos);

        return ResultVoUtil.success(vos);
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model,
                            @PathVariable("userId") Integer userId){


        User user = userService.getById(userId);
        ViewObject vo = new ViewObject();

        //这个人的信息
        vo.set("user",user);

        //这个人有多少条回答
        vo.set("commentCount",commentService.getUserAnswerCount(userId));

        //被多少个人关注
        vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER.getValue(),userId));

        //关注了多少个人
        vo.set("followeeCount",followService.getFolloweeCount(EntityType.ENTITY_USER.getValue(),userId));

        //当前用户有没有关注TA
        if (hostHolder.getUsers() != null){
            model.addAttribute("localUser",hostHolder.getUsers());
            vo.set("followed",followService.isFollow(EntityType.ENTITY_USER.getValue(),userId,hostHolder.getUsers().getUserId()));
        }else {
            vo.set("followed",false);
        }

        model.addAttribute("profileUser",vo);
        return "profile";
    }

    @RequestMapping(path = {"/editUser/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String editUser(@PathVariable("userId") Integer userId,
                           Model model) {
        if (hostHolder.getUsers() != null) {
            model.addAttribute("localUser", hostHolder.getUsers());
        }
        return "editUser";
    }

    @RequestMapping(path = {"/editUserName/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String editUserName(@RequestParam("name") String name,
                               @PathVariable("userId") Integer userId, Model model) {
        boolean res = userService.editName(userId, name) > 0;

        model.addAttribute("msg", res ? "修改成功" : "修改失败");

        return "/editUser/" + userId;
    }

    @PostMapping(path = {"/editUserPass/{userId}"})
    public String editPass(@RequestParam("oldPass") String oldPass,
                           @RequestParam("newPass") String newPass,
                           @PathVariable("userId") Integer userId,
                           Model model) {
        boolean res = userService.verifyPass(hostHolder.getUsers().getName(), oldPass);

        if (res) {
            boolean editRes = userService.editPass(newPass, userId) > 0;
            if (editRes) {
                model.addAttribute("msg", "修改成功");
            } else {
                model.addAttribute("msg", "修改失败");
            }
        }

        return "/editUser/" + userId;
    }

    @PostMapping(path = {"/validatorUser/{userId}"})
    public String validator(@RequestParam("txtUserName") String txtUserName,
                            @RequestParam("password") String password,
                            @RequestParam("txtSecretCode") String txtSecretCode,
                            @RequestParam("proofName") String proofName,
                            @PathVariable("userId") Integer userId,
                            Model model) {
        boolean res = ValidatorUtil.validatorReal(txtUserName, password, txtSecretCode, proofName);

        if (res) {
            Proof proof = new Proof();
            if (proofName.equals("学生")) {
                proof.setType(ProofType.STUDENT.getValue());
            } else if (proofName.equals("教师")) {
                proof.setType(ProofType.TEACHER.getValue());
            }
            proof.setTypeName(proofName);
            proof.setUserId(userId);
            proofService.addProof(proof);
            model.addAttribute("msg", "认证成功");
        } else {
            model.addAttribute("msg", "认证失败，你可能输错了");
        }

        return "/editUser/" + userId;
    }
}
