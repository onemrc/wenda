package com.demo.wenda.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * http请求返回的最外层对象
 */
@Data
public class ResultVo implements Serializable {

    private static final long serialVersionUID = -4532782707965381302L;

    /** 状态码 */
    private  Integer code;

    /** 提示信息 */
    private  String massage;

    /** 数据 */
    private  Object data;

}
