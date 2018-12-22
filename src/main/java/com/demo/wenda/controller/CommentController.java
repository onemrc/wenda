package com.demo.wenda.controller;

import com.demo.wenda.async.EventModel;
import com.demo.wenda.async.EventProducer;
import com.demo.wenda.domain.Comment;
import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.CommentStatus;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.service.CommentService;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 评论
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private HostHolder hostHolder;

    private CommentService commentService;

    private QuestionService questionService;

    private final EventProducer eventProducer;

    private final UserService userService;

    @Autowired
    public CommentController(HostHolder hostHolder, CommentService commentService, QuestionService questionService, EventProducer eventProducer, UserService userService) {
        this.hostHolder = hostHolder;
        this.commentService = commentService;
        this.questionService = questionService;
        this.eventProducer = eventProducer;
        this.userService = userService;
    }

    /*
                添加回答
                回答——>对问题的评论
                 */
    @RequestMapping(value = "/addAnswer", method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {

        try {
            Comment comment = new Comment();
            //当前用户
            User hostUser = hostHolder.getUsers();

            if (hostUser == null) {
                return ConverterUtil.getJSONString(999);
            }

//            //用户在该问题下已经有回答
//            if (commentService.queryIdExist(questionId, EntityType.QUESTION.getValue(), hostUser.getUserId()) != null) {
//                return ConverterUtil.getJSONString(CommentStatus.IS_EXIST.getCode(), CommentStatus.IS_EXIST.getMsg());
//            }

            comment.setEntityId(questionId);
            comment.setContent(content);

//            if (hostHolder.getUsers() != null) {
//                comment.setUserId(hostHolder.getUsers().getUserId());
//            } else {
//                //滚去登录
//                return "redirect:/to_login?next="+ request.getRequestURI();
//            }

            comment.setCreateDate(new Date());
            comment.setEntityType(EntityType.QUESTION.getValue());
            comment.setEntityId(questionId);
            comment.setCommentStatus(CommentStatus.DELETED.getCode());
            comment.setUserId(hostUser.getUserId());

            commentService.addComment(comment);
            //该问题回答数+1
            questionService.incrCommentCount(questionId);
            logger.info("添加回答成功：questionId={}", questionId);

            //异步发送一个私信给TA评论的人
            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setActorId(hostUser.getUserId())
                    .setEntityType(EntityType.QUESTION.getValue())
                    .setEntityId(questionId)
                    .setEntityOwnerId(questionService.getById(questionId).getUserId()));



        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加回答失败:{}", e.getMessage());
            return ConverterUtil.getJSONString(1, "添加回答失败");
        }

        return "redirect:question/" + questionId;
    }


    /**
     * 删除一条回答
     *
     * @param answerId
     * @param questionId 所属问题id
     * @return
     */
    @RequestMapping(value = "/deleteAnswer", method = RequestMethod.POST)
    public String addComment(@RequestParam("answerId") int answerId,
                             @RequestParam("questionId") int questionId) {
        try {

            //改变回答状态
            commentService.deleteCommentByEntity(answerId, EntityType.QUESTION.getValue(), hostHolder.getUsers().getUserId(), CommentStatus.DELETED.getCode());


        } catch (Exception e) {
            logger.error("删除回答失败:{}", e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
