package com.demo.wenda.controller;

import com.demo.wenda.domain.Comment;
import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.enums.CommentStatus;
import com.demo.wenda.enums.EntityTypeEnum;
import com.demo.wenda.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping(value = "/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;


    @Autowired
    CommentService commentService;



    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){

        try{
            Comment comment = new Comment();
            comment.setEntityId(questionId);
            comment.setContent(content);

            if (hostHolder.getUsers() != null){
                comment.setUserId(hostHolder.getUsers().getUserId());
            }

            comment.setCreateDate(new Date());
            comment.setEntityType(EntityTypeEnum.ENTITY_QUESTION.getCode());
            comment.setEntityId(questionId);
            comment.setCommentStatus(CommentStatus.DELETED.getCode());

            commentService.addComment(comment);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("增加评论失败:{}",e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
