package com.demo.wenda.domain;

import lombok.Data;

/**
 * create by: one
 * create time:2018/12/23 21:26
 * 描述：身份信息
 */
@Data
public class Proof {
    private Integer id;
    private Integer type;
    private String typeName;
    private Integer userId;
}
