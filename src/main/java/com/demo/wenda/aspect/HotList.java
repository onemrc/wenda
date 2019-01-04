package com.demo.wenda.aspect;


import com.demo.wenda.service.CommentService;
import com.demo.wenda.service.QuestionService;
import com.demo.wenda.service.RedisService;
import com.demo.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;


/**
 * create by: one
 * create time:2019/1/4 18:27
 * 描述：热榜处理 定时任务
 */
@Component
public class HotList {

    private final RedisService redisService;

    private final CommentService commentService;

    private final QuestionService questionService;

    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Autowired
    public HotList(RedisService redisService, CommentService commentService, QuestionService questionService) {
        this.redisService = redisService;
        this.commentService = commentService;
        this.questionService = questionService;
    }

    /**
     * 更新热榜
     * <p>
     * 用三个有序集合实现：
     * set1:记录所有问题的得分
     * set2:记录所有问题的被浏览数
     * set3:记录所有问题底下的回答的总点赞数
     * set4:记录所有问题底下的总评论数
     * <p>
     * 每隔一定时间，更新set1所有问题的得分，得到最新热门榜单
     */
    @Scheduled(fixedRate = 5000)
    public void updateHotList() throws ParseException {
        //取出问题总浏览数集合
        Set<String> lookCountSet = redisService.zrange(RedisKeyUtil.getQuestionLookcount(), 0, 1000);

        //取出问题总点赞数集合
        Set<String> likeCountSet = redisService.zrange(RedisKeyUtil.getQuestionLikecount(), 0, 1000);

        //取出问题总评论数集合
        Set<String> commentCountSet = redisService.zrange(RedisKeyUtil.getQuestionCommentcount(), 0, 1000);

        //更新排行榜
        for (String questionId : lookCountSet) {

            double likeCount = redisService.zscore(RedisKeyUtil.getQuestionLikecount(), questionId) == null ? 0 : redisService.zscore(RedisKeyUtil.getQuestionLikecount(), questionId);//该问题总点赞数
            double lookCount = redisService.zscore(RedisKeyUtil.getQuestionLookcount(), questionId) == null ? 0 : redisService.zscore(RedisKeyUtil.getQuestionLookcount(), questionId);//该问题浏览数
            double commentCount = redisService.zscore(RedisKeyUtil.getQuestionCommentcount(), questionId) == null ? 0 : redisService.zscore(RedisKeyUtil.getQuestionCommentcount(), questionId);//该问题评论数
            //问题投票数
            float P = (float) (lookCount + likeCount * 10 + commentCount * 20);

            if (P == 0) {
                P = (float) 0.1;
            }

            //获取时间差
            String newCommentTime = commentService.getQuestionLastCommentTime(Integer.valueOf(questionId));
            if (newCommentTime == null) {//没有评论
                newCommentTime = questionService.getCreateTime(Integer.valueOf(questionId));
            }
            String nowDate = simpleFormat.format(new Date());

            long from = simpleFormat.parse(newCommentTime).getTime();
            long to = simpleFormat.parse(nowDate).getTime();

            float T = ((from - to) / (1000 * 60 * 60));

            //重力因子
            float G = (float) 1.8;

            //计算热度分数
            float score = (float) ((P - 1) / Math.pow((T + 2), G));

            redisService.zadd(RedisKeyUtil.getHotList(), score, questionId);
        }
    }
}
