package com.demo.wenda.redis;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
@ConfigurationProperties(prefix = "spring.redis.jedis.pool",ignoreInvalidFields = true)  //读配置
@Data
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    //    @Value("${jedis.pool.maxIdle}")
    private int maxIdle;

    //    @Value("${max-wait}")
    private long maxWait;

    //    @Value("${max-active}")
    private int maxActive;

    //    @Value("${min-idle}")
    private int minIdle;

}
