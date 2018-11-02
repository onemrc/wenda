package com.demo.wenda.enums;


import lombok.Getter;

@Getter
public enum  EntityTypeEnum {
    ENTITY_QUESTION(0,"问题");

    private Integer code;
    private String msg;

    EntityTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
