package com.demo.wenda.async;

import com.demo.wenda.enums.EventType;

import java.util.List;

/**
 * create by: one
 * create time:2018/12/2 21:57
 * 描述：TODO
 */
public interface EventHandler {
    void doEventHandle(EventModel eventModel);

    List<EventType> getSupportEventTypes();
}
