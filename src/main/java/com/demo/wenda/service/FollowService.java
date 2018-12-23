package com.demo.wenda.service;

import com.demo.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {


    private final RedisService redisService;

    @Autowired
    public FollowService(RedisService redisService) {
        this.redisService = redisService;
    }


    /**
     * 关注
     * 关注的发起者是用户
     * 被关注的实体可以是用户、也可以是问题等
     * @param userId    当前用户id
     * @param entityId  被关注实体的id
     * @param entityType 被关注实体的type
     * @return
     */
    public boolean follow(int userId, int entityId, int entityType) {

        //关注者的key
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        //被关注的对象 key
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Date date = new Date();
        Jedis jedis = redisService.getNewJedis();

        //开启事务
        Transaction transaction = redisService.multi(jedis);


        //更新某个实体下的粉丝列表（score == 当前时间）
        transaction.zadd(followerKey, date.getTime(), userId + "");

        //更新某个实体下关注的类型列表（score == 当前时间）
        transaction.zadd(followeeKey, date.getTime(), entityId + "");

        //执行事务
        List<Object> res = redisService.exec(transaction, jedis);


        return res.size() == 2 && (Long) res.get(0) > 0 && (Long) res.get(1) > 0;
    }


    /*
    获取某实体被关注的数量
     */
    public Long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisService.zcard(followerKey);
    }

    /*
    获取用户关注的实体数量
     */
    public Long getFolloweeCount(int entityType, int entityId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityId,entityType);
        return redisService.zcard(followeeKey);
    }

    /*
    获取某实体 区间内 关注的实体id
     */
    public Set<String> getFollowers(int entityType, int entityId, long start, long end){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisService.zrange(followerKey,start,end);
    }

    /*
    获取某实体 被谁关注了
     */
    public Set<String> getFollowees(int entityType, int entityId, long start, long end){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityId, entityType);
        return redisService.zrange(followeeKey,start,end);
    }

    /**
     * 某用户是否关注了某实体
     * @param entityType
     * @param userId
     * @return
     */
    public boolean isFollow(int entityType,int entityId,int userId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        return redisService.zrank(followerKey,userId+"") != null ;
    }

    /**
     * 某用户取消对某实体的关注
     * @param entityType
     * @param entityId
     * @param userId
     * @return
     */
    public boolean unFollow(int entityType,int entityId,int userId){
        //关注者的key
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        //被关注的对象 key
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);


        Jedis jedis = redisService.getNewJedis();
        //开启事务
        Transaction transaction = redisService.multi(jedis);

        transaction.zrem(followerKey,userId+"");

        transaction.zrem(followeeKey,entityId+"");

        List<Object> res = redisService.exec(transaction, jedis);

        return res.size() == 2 && (Long) res.get(0)> 0 && (Long) res.get(1) > 0;
    }
}
