package com.demo.wenda.service;

import com.demo.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;

/**
 * create by: one
 * create time:2018/12/1 12:43
 * 描述：点赞服务
 */
@Service
public class LikeService {

    private final RedisService redisService;

    @Autowired
    public LikeService(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 给某个评论点赞
     * @param userId 用户id
     * @param commentId 评论id
     * @param entityType 评论类型
     * @return
     */
    public Long commentLike(int userId,int commentId,int entityType){

        //被点赞对象的key（某个评论被谁点赞了）
        String commentLikeKey = RedisKeyUtil.getCommentLikeKey(entityType,commentId);

        //点赞者的key（我点赞了哪些评论）
        String userLike = RedisKeyUtil.getUserLikeKey(userId,entityType);

        Date date = new Date();
        Jedis jedis = redisService.getNewJedis();

        //开启事务
        Transaction transaction = redisService.multi(jedis);

        //更新某评论下的userId列表
        transaction.zadd(commentLikeKey,date.getTime(),userId+"");

        //更新该用户的点赞列表
        transaction.zadd(userLike,date.getTime(),commentId+"");

        //执行事务
        List<Object> res = redisService.exec(transaction, jedis);

        //获取点赞人数
        return redisService.zcard(commentLikeKey);

//        return res.size() == 2 && (Long) res.get(0) > 0 && (Long) res.get(1) > 0;
    }

    /**
     * 获取某评论实体下的点赞数
     * @param entityType
     * @param entityId
     * @return
     */
    public Long getCommentLikeCount(int entityType,int entityId){
        String commentLikeKey = RedisKeyUtil.getCommentLikeKey(entityType,entityId);
        return redisService.zcard(commentLikeKey);
    }

    /**
     * 获取某用户点赞的实体数
     * @param userId
     * @param entityType
     * @return
     */
    public Long getUserLikeCount(int userId,int entityType){
        String userLike = RedisKeyUtil.getUserLikeKey(userId,entityType);

        return redisService.zcard(userLike);
    }


    /**
     * 取消某用户点的赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean cancelLikeToAswer(int userId,int entityType,int entityId){
        String userLike = RedisKeyUtil.getUserLikeKey(userId,entityType);

        String commentLikeKey = RedisKeyUtil.getCommentLikeKey(entityType,entityId);

        Jedis jedis = redisService.getNewJedis();

        //开启事务
        Transaction transaction = redisService.multi(jedis);


        //删除用户点赞列表下的entityId
        transaction.zrem(userLike,entityId+"");

        //删除某实体下点赞的用户id
        transaction.zrem(commentLikeKey,userId+"");

        //执行事务
        List<Object> res = redisService.exec(transaction, jedis);

        return res.size() == 2 && (Long) res.get(0) > 0 && (Long) res.get(1) > 0;
    }

    /**
     * 某用户被点赞总数
     * @param userId
     * @return
     */
    public String getUserLikedCount(int userId){
        String key = RedisKeyUtil.getUserLikecount(userId);
        return redisService.get(key);
    }
}
