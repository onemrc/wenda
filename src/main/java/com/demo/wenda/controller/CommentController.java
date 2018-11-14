package com.demo.wenda.controller;

import com.demo.wenda.domain.Comment;
import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.enums.CommentStatus;
import com.demo.wenda.enums.EntityType;
import com.demo.wenda.service.CommentService;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private HostHolder hostHolder;

    private CommentService commentService;

    @Autowired
    public CommentController(HostHolder hostHolder, CommentService commentService) {
        this.hostHolder = hostHolder;
        this.commentService = commentService;
    }

    /*
    添加回答
    回答——>对问题的评论
     */
    @RequestMapping(value = "/addAnswer", method = RequestMethod.POST)
    public String addComment(HttpServletRequest request,
                            @RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {

        try {
            Comment comment = new Comment();
            comment.setEntityId(questionId);
            comment.setContent(content);

            if (hostHolder.getUsers() != null) {
                comment.setUserId(hostHolder.getUsers().getUserId());
            } else {
                //滚去登录
                return "redirect:/to_login?next="+ request.getRequestURI();
            }

            comment.setCreateDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION.getValue());
            comment.setEntityId(questionId);
            comment.setCommentStatus(CommentStatus.DELETED.getCode());

            commentService.addComment(comment);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加回答失败:{}", e.getMessage());
            return ConverterUtil.getJSONString(1, "添加回答失败");
        }

        return "redirect:/question/" + questionId;
    }
}
