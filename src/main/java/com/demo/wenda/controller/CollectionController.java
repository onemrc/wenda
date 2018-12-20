package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.StatusCodeEnum;
import com.demo.wenda.service.CollectionService;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * create by: one
 * create time:2018/12/1 17:40
 * 描述：收藏
 */
@Controller
public class CollectionController {

    private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);


    private final CollectionService collectionService;


    private final HostHolder hostHolder;

    @Autowired
    public CollectionController(CollectionService collectionService, HostHolder hostHolder) {
        this.collectionService = collectionService;
        this.hostHolder = hostHolder;
    }

    /**
     * 当前用户对某个问题进行收藏
     * @param questionId 问题id
     * @return
     */
    @RequestMapping(value = "/addQuestionCollection", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestParam("questionId") int questionId){
        //当前用户
        User hostUser = hostHolder.getUsers();

        if (hostUser == null) {
            return ConverterUtil.getJSONString(999);
        }

        boolean res = collectionService.add(hostUser.getUserId(),questionId, EntityType.QUESTION.getValue());

        return ConverterUtil.getJSONString(res ? StatusCodeEnum.OK : StatusCodeEnum.COLLECTION_FAIL);
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
}
