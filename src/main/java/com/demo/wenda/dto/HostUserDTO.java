package com.demo.wenda.dto;

import lombok.Data;

/**
 * create by: one
 * create time:2018/12/10 17:32
 * 描述：一些用户数据
 */
@Data
public class HostUserDTO {
    //用户名
    private String userName;

    //个人介绍
    private String introduction;

    //回答数
    private Long commentCount;

    //粉丝数
    private Long followerCount;

    //收藏数
    private Integer collectionCount;
}
