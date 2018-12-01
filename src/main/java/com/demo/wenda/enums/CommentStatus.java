package com.demo.wenda.enums;

import lombok.Getter;

@Getter
public enum  CommentStatus {
    NORMAL(0,"正常"),
    DELETED(1,"已删除"),
    IS_EXIST(2,"已有评论");

    private Integer code;
    private String msg;

    CommentStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
