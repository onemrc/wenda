package com.demo.wenda.domain;

import lombok.Data;

/**
 * 点赞表
 *
 * 记录某个回答下，点赞的人
 */
@Data
public class UserAnswerAgree {

    private Integer userAnswerAgreeId;

    private Integer answerId;

    private Integer userId;
}
