package com.demo.wenda.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginVO {

    @NotNull
    private String str;

    @NotNull
    private String password;
}
