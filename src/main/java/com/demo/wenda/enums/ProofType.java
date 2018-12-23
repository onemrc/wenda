package com.demo.wenda.enums;

import lombok.Getter;

/**
 * create by: one
 * create time:2018/12/24 0:22
 * 描述：身份类别
 */
@Getter
public enum ProofType {
    STUDENT(0),
    TEACHER(1);

    private Integer value;

    ProofType(Integer value) {
        this.value = value;
    }
}
