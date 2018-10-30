package com.demo.wenda.domain;

import lombok.Data;

/**
 * 用户表
 */
@Data
public class User {

    //用户id
    private Integer userId;

    //用户名
    private String name;

    //用户密码
    private String password;

    //加密密码用
    private String salt;

    //用户头像地址
    private String headUrl;

    //个人介绍
    private String introduction;

    //用户性别
    private Integer sex;

    //所属学院
    private String department;

    //专业
    private String profession;

    //入学年份
    private String startYear;

    //毕业年份
    private String endYear;


    //手机号
    private Long phone;

    //邮箱
    private String email;
}
