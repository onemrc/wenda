package com.demo.wenda.domain;

import lombok.Data;

import java.util.Date;

/**
 * 评论表
 *
 * 记录某个回答下，评论的人和内容
 */
@Data
public class userAnswerComment {

    private Integer userAnswerCommentId;

    private Integer answer_id;
    private Integer user_id;

    //评论内容
    private String content;

    //评论时间
    private Date commentTime;

}
