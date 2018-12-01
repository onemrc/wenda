package com.demo.wenda.domain;

import lombok.Data;

import java.util.Date;

/**
 * 记录对某个实体的评论
 *
 * 如：对某个回答的评论
 * 对某个评论的回复
 */
@Data
public class Comment {

    private Integer commentId;

    private Integer entityId;

    private int entityType;

    private Integer userId;

    private int commentStatus;

    private Date createDate;

    private String content;

}
