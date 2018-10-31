package com.demo.wenda.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getMaxActive());
        jedisPoolConfig.setMinIdle(redisConfig.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getMaxWait());

        return new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword());
    }
}
