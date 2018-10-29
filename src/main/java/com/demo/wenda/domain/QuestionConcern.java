package com.demo.wenda.domain;

import lombok.Data;

/**
 * 问题关注表
 */
@Data
public class QuestionConcern {

    private Integer questionConcernId;

    private Integer questionId;

    private Integer userId;


}
