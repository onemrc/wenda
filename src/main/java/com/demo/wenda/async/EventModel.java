package com.demo.wenda.async;

import com.demo.wenda.enums.EventType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * create by: one
 * create time:2018/12/2 21:36
 * 描述：EventModel
 */
@Getter
public class EventModel {
    private EventType eventType;

    //事件触发者id
    private int actorId;

    //事件实体类型
    private int entityType;

    //事件实体id
    private int entityId;

    //事件实体的用户id
    private int entityOwnerId;

    //其他需要传的变量
    private Map<String,String> exts = new HashMap<String,String>();

    public EventModel() {
    }

    public  EventModel(EventType eventType) {
        this.eventType = eventType;
    }

    public EventModel setExts(String key, String value) {
        exts.put(key,value);
        return this;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }
}
