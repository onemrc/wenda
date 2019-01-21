package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Question;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.StatusCodeEnum;
import com.demo.wenda.service.CollectionService;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * create by: one
 * create time:2018/12/1 17:40
 * 描述：收藏
 */
@Controller
public class CollectionController {

    private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);


    private final CollectionService collectionService;

    private final QuestionService questionService;

    private final UserService userService;

    private final HostHolder hostHolder;

    @Autowired
    public CollectionController(CollectionService collectionService, QuestionService questionService, UserService userService, HostHolder hostHolder) {
        this.collectionService = collectionService;
        this.questionService = questionService;
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    /**
     * 当前用户对某个问题进行收藏
     * @param questionId 问题id
     * @return
     */
    @RequestMapping(value = "/addQuestionCollection", method = RequestMethod.POST)
    public String add(@RequestParam("questionId") int questionId){
        //当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        boolean res = collectionService.add(hostUser.getUserId(),questionId, EntityType.QUESTION.getValue());

        return "redirect:/question/" + questionId;
    }

    /**
     * 移除收藏问题
     * @param questionId 问题id
     * @return
     */
    @RequestMapping(value = "/removeQuestionCollection", method = RequestMethod.POST)
    @ResponseBody
    public String remove(@RequestParam("questionId") int questionId){
        //当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        boolean res = collectionService.remove(hostUser.getUserId(),questionId, EntityType.QUESTION.getValue());

        return ConverterUtil.getJSONString(res ? StatusCodeEnum.OK : StatusCodeEnum.COLLECTION_FAIL);
    }

    @RequestMapping(value = "/myCollection", method = RequestMethod.GET)
    public String myCollection(Model model) {
        //当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }


        List<ViewObject> vos = new ArrayList<>();

        List<String> questionIds = collectionService.getEntityIds(hostUser.getUserId(), EntityType.QUESTION.getValue());

        for (String questionId : questionIds) {
            ViewObject vo = new ViewObject();
            Question question = questionService.getById(Integer.valueOf(questionId));
            User user = userService.getById(question.getUserId());
            vo.set("question", question);
            vo.set("user", user);
            vos.add(vo);
        }


        model.addAttribute("vos", vos);
        model.addAttribute("localUser", hostUser);

        return "result";
    }
}
