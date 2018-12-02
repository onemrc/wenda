package com.demo.wenda.domain;

import lombok.Data;

import java.util.Date;

/**
 * create by: one
 * create time:2018/12/1 17:44
 * 描述：收藏表
 */
@Data
public class Collections {
    private Integer CollectionId;

    private Integer entityId;

    private int entityType;

    private Integer userId;

    private Date createDate;

}
