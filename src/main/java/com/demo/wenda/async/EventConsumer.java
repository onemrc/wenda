package com.demo.wenda.async;


import com.alibaba.fastjson.JSON;
import com.demo.wenda.enums.EventType;
import com.demo.wenda.service.RedisService;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by: one
 * create time:2018/12/2 21:59
 * 描述：事件消费者
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private final RedisService redisService;


    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();

    private ApplicationContext applicationContext;


    @Autowired
    public EventConsumer(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        //找出所有实现了 EventHandler 接口的类
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null){
            for (Map.Entry<String,EventHandler> entry : beans.entrySet()){
                //找出这个Handler关注的 Event
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes){
                    if (!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key  = RedisKeyUtil.getEventQueueKey();

                    //将队列中的event取出
                    List<String> event = redisService.brpop(0,key);

                    for (String message : event){
                        if (message.equals(key)){
                            continue;
                        }

                        //将eventModel 从JSON 中解析出来
                        EventModel eventModel = ConverterUtil.stringToBean(message,EventModel.class);

                        if (!config.containsKey(eventModel.getEventType())){
                            logger.error("不能识别的事件");
                            continue;
                        }

                        //处理这些event
                        for (EventHandler handler : config.get(eventModel.getEventType())){
                            handler.doEventHandle(eventModel);
                        }
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
