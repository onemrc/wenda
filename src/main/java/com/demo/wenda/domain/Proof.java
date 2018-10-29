package com.demo.wenda.domain;


import lombok.Data;

/**
 * 身份验证表
 */
@Data
public class Proof {

    private Integer proofId;

    //用户id
    private Integer user_id;

    //真实姓名
    private String real_name;

    //类别（学生、老师、管理员）
    private String type;

}
