package com.demo.wenda.dto;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionDTO {

    private String userName;

    private String title;

    private String createTime;

    private String content;

    private Integer answerCount;

}
