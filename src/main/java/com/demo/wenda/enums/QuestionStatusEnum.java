package com.demo.wenda.enums;


import lombok.Getter;

@Getter
public enum  QuestionStatusEnum {
    IS_ANONYMOUS(0,"匿名"),
    NOT_ANONYMOUS(1,"不匿名");


    private Integer code;
    private String msg;

    QuestionStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
