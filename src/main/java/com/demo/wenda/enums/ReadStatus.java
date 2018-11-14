package com.demo.wenda.enums;

import lombok.Getter;

@Getter
public enum ReadStatus {
    NOT_READ(0,"未读"),
    HAVE_READ(1,"已读");

    private Integer code;
    private String msg;

    ReadStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
