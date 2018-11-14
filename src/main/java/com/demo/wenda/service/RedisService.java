package com.demo.wenda.service;

import com.demo.wenda.redis.KeyPrefix;
import com.demo.wenda.utils.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;

@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    private JedisPool jedisPool;

    @Autowired
    public RedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }



    public Jedis getNewJedis(){
        return jedisPool.getResource();
    }


    /*
    从redis 获取对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> tClass) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            //真正存过去的key
            key = prefix.getPrefix() + key;

            String str = jedis.get(key);
            T t = ConverterUtil.stringToBean(str, tClass);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /*
    set 进redis
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //将对象转换为String ，方便存进redis
            String str = ConverterUtil.beanToString(value);

            if (str == null || str.length() <= 0) {//传了一个空的对象
                return false;
            }

            //存进redis的key
            key = prefix.getPrefix() + key;

            int expireDate = prefix.getExpireDate();
            if (expireDate <= 0) {
                jedis.set(key, str);
            } else {
                jedis.setex(key, expireDate, str);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("redis set 异常：{}", e.getMessage());
        } finally {
            returnToPool(jedis);
        }
        return false;
    }


    /*
    判断key是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            key = prefix.getPrefix() + key;
            return jedis.exists(key);
        } finally {
            returnToPool(jedis);
        }
    }

    /*
     自减1
      */
    public Long decr(KeyPrefix prefix, String key) {// -1
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            key = prefix.getPrefix() + key;

            return jedis.decr(key);
        } finally {
            returnToPool(jedis);//释放
        }
    }

    /*
    自增1
     */
    public Long incr(KeyPrefix prefix, String key) {// +1
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            key = prefix.getPrefix() + key;

            return jedis.incr(key);
        } finally {
            returnToPool(jedis);//释放
        }
    }

    //  将资源还给连接池
    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    /*
    开启事务
     */
    public Transaction multi(Jedis jedis){
        try {
             return jedis.multi();
        } catch (Exception e){
            logger.error("开启事务异常：{}",e.getMessage());
        }
        return null;
    }


    /*
    执行事务
    返回每条命令的回复
     */
    public List<Object> exec(Transaction transaction,Jedis jedis){
        try {

            return transaction.exec();
        } catch (Exception e){
            logger.error("执行事务异常：{}",e.getMessage());
        }finally {
            if (transaction != null){
                try {
                    transaction.close();
                } catch (IOException e) {
                    logger.error("事务关闭异常：{}",e.getMessage());
                }
            }
            returnToPool(jedis);
        }
        return null;
    }

    /*
    获取集合中元素的数量
     */
    public Long zcard(String key){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.zcard(key);
        }finally {
            returnToPool(jedis);
        }
    }



//    public static void main(String[] args) {
//        RedisService redisService = new RedisService();
//        User  user =  redisService.get(UserKey.token,"df015cbe3972406b9b40d96ecac38b80", User.class);
//        System.out.println(user.getName());
//
//    }
}
