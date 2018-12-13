package com.demo.wenda.enums;

import lombok.Getter;

@Getter
public enum StatusCodeEnum {
    OK(200,"成功"),
    ERROR(900,"服务器异常"),

    COLLECTION_FAIL(111,"收藏失败"),
    EMAIL_EXIST(301,"该邮箱已被注册"),
    PHONE_EXIST(302,"该手机号已被注册")
    ;


    private Integer code;
    private String massage;


    StatusCodeEnum(Integer code, String massage) {
        this.code = code;
        this.massage = massage;
    }
}
