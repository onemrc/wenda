package com.demo.wenda.domain;

import lombok.Data;

import java.util.Date;

/**
 * 回答表
 */
@Data
public class Answer {
    private Integer answerId;

    private Integer questionId;
    private Integer userId;

    //是否匿名回答
    private Integer anonymous;

    //评论数
    private Integer commentCount;

    //点赞数
    private Integer agreeCount;

    //发布时间
    private Date createTime;

    //最后修改时间
    private Date updateTime;
}
