package com.demo.wenda.enums;

import lombok.Getter;

/**
 * 事件类型
 */
@Getter
public enum EventType {
    LIKE(0),
    FOLLOW(1),
    UNFOLLOW(2),
    COMMENT(3),

    ;

    private Integer value;


    EventType(Integer value) {
        this.value = value;
    }


}
