package com.demo.wenda.async;

import com.alibaba.fastjson.JSON;
import com.demo.wenda.service.RedisService;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by: one
 * create time:2018/12/2 21:43
 * 描述：异步服务 提供者
 */
@Service
public class EventProducer {

    private final RedisService redisService;

    @Autowired
    public EventProducer(RedisService redisService) {
        this.redisService = redisService;
    }

    public void fireEvent(EventModel eventModel){
//        String json = ConverterUtil.getJSONString(eventModel);
        String json = JSON.toJSONString(eventModel);
        String key = RedisKeyUtil.getEventQueueKey();
        redisService.lpush(key,json);
    }
}
