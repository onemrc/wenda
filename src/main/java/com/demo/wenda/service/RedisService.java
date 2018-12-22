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
import java.util.Set;

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
    获取对象
    */
    public String get(String key) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            return jedis.get(key);
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

    public String set(String key, String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return jedis.set(key,value);
        } finally {
            returnToPool(jedis);
        }
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
  自减1
   */
    public Long decr(String key) {// -1
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

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

    /*
 自增1
  */
    public Long incr(String key) {// +1
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

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

    /*
    将一个或多个值插入到链表头部
     */
    public Long lpush(String key,String value){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.lpush(key,value);
        }finally {
            returnToPool(jedis);
        }
    }

    /*
    移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    public List<String> brpop(int timeout, String... keys){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.brpop(timeout,keys);
        }finally {
            returnToPool(jedis);
        }
    }

    /*
    将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略
     */
    public Long sadd(String key, String... members){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.sadd(key,members);
        }finally {
            returnToPool(jedis);
        }
    }

    /*
    返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
     */
    public Set<String> smembers(String key){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.smembers(key);
        }finally {
            returnToPool(jedis);
        }
    }

    /*
    返回有序集中，指定区间内的成员。
     */
    public Set<String> zrange(String key, long start, long end){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrange(key,start,end);
        }finally {
            returnToPool(jedis);
        }
    }

    /*
    判断成员元素是否是集合的成员
     */
    public Boolean sismember(String key, String member){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.sismember(key,member);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 返回有序集中指定成员的排名，不存在返回nio
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key, String member){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrank(key,member);
        }finally {
            returnToPool(jedis);
        }
    }

    /*
    返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定
     */
    public List<String> lrange(String key, long start, long end){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();

            return jedis.lrange(key,start,end);
        }finally {
            returnToPool(jedis);
        }
    }

//    /**
//     * 移除有序集中的一个或多个成员，不存在的成员将被忽略
//     * @param key key
//     * @param members 元素
//     * @return
//     */
//    public Long zrem(String key,String... members){
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//
//            return jedis.zrem(key,members);
//        }finally {
//            returnToPool(jedis);
//        }
//    }



//    public static void main(String[] args) {
//        RedisService redisService = new RedisService();
//        User  user =  redisService.get(UserKey.token,"df015cbe3972406b9b40d96ecac38b80", User.class);
//        System.out.println(user.getName());
//
//    }
}
