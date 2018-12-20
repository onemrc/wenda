package com.demo.wenda.async;

import com.demo.wenda.enums.EventType;

import java.util.List;

/**
 * create by: one
 * create time:2018/12/2 21:57
 * 描述：专门用于处理各种Event接口
 */
public interface EventHandler {
    //处理事件
    void doEventHandle(EventModel eventModel);

    //注册
    List<EventType> getSupportEventTypes();
}
