package com.demo.wenda.domain;

import lombok.Data;

import java.util.Date;

/**
 * create by: one
 * create time:2018/12/22 16:42
 * 描述：发现
 */
@Data
public class Find {
    private int id;
    private int type;
    private int userId;
    private Date createDate;
    private String data;
}
