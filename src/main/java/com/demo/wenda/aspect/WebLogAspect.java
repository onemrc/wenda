package com.demo.wenda.aspect;

import com.demo.wenda.controller.CollectionController;
import com.demo.wenda.service.RedisService;
import com.demo.wenda.utils.RedisKeyUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * create by: one
 * create time:2018/12/10 15:03
 * 描述：请求日志切面
 */
@Aspect
@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    private final RedisService redisService;

    @Autowired
    public WebLogAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    @Pointcut("execution(public * com.demo.wenda.controller.QuestionController.questionDetail(..))")
    public void questionDetail(){}

    @Pointcut("execution(public * com.demo.wenda.controller.CommentController.addComment(..))")
    public void questionComment() {
    }


    /**
     * 每次请求问题详情后，该问题浏览数 +1
     */
    @After("questionDetail()")
    public void doAfterQuestionDetail(JoinPoint joinPoint){
        //拿到对应的questionId
        Integer questionId = null;
        Object[] objects = joinPoint.getArgs();
        for (Object o:objects){
            if (o instanceof Integer){
                questionId = (Integer) o;
            }
        }
        if (questionId == null){
            logger.error("获取不到对应的questionId");
        }else {
            String key = RedisKeyUtil.getQuestionLook(questionId);
            redisService.incr(key);

            //该问题浏览榜 浏览数+1
            redisService.zincrby(RedisKeyUtil.getQuestionLookcount(), 1, questionId + "");
        }
    }

    /**
     * 每次添加评论，该问题总评论数+1
     */
    @After("questionComment()")
    public void doAfterquestionComment(JoinPoint joinPoint) {
        //拿到对应的questionId
        Integer questionId = null;
        Object[] objects = joinPoint.getArgs();
        for (Object o : objects) {
            if (o instanceof Integer) {
                questionId = (Integer) o;
            }
        }
        if (questionId == null) {
            logger.error("获取不到对应的questionId");
        } else {

            //该问题评论榜 评论数+1
            redisService.zincrby(RedisKeyUtil.getQuestionCommentcount(), 1, questionId + "");
        }
    }
}
