package com.demo.wenda.domain;

import lombok.Data;

/**
 * 问题分类标签
 */
@Data
public class Tag {

    private Integer tagId;

    //标签名
    private String tagName;
}
