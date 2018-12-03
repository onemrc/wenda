package com.demo.wenda.enums;

import lombok.Getter;

@Getter
public enum  EntityType {
    ENTITY_QUESTION(0),
    ENTITY_USER(1),
    ENTITY_COMMENT(2),
    ENTITY_ANSWER(3)
    ;

    private Integer value;


    EntityType(Integer value) {
        this.value = value;
    }

}
