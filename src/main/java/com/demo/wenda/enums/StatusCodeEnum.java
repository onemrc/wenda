package com.demo.wenda.enums;

import lombok.Getter;

@Getter
public enum StatusCodeEnum {
    OK(200,"成功"),

    COLLECTION_FAIL(111,"收藏失败")
    ;


    private Integer code;
    private String massage;


    StatusCodeEnum(Integer code, String massage) {
        this.code = code;
        this.massage = massage;
    }
}
