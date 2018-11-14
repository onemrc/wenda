package com.demo.wenda.domain;

import lombok.Data;

import java.util.Date;

/**
 * 问题表
 */
@Data
public class Question {

    private Integer questionId;

    //用户id
    private Integer userId;

    //标签id
    private Integer tagId;

    //是否匿名
    private Integer anonymous;

    //标题
    private String title;

    //问题内容
    private String content;

    //问题发布时间
    private Date createTime;


    //回答数
    private Integer commentCount;

    //被浏览数
    private Integer lookCount;
}
