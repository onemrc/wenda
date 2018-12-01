package com.demo.wenda.utils;

import com.demo.wenda.enums.StatusCodeEnum;
import com.demo.wenda.vo.ResultVo;

public class ResultVoUtil {

    public static ResultVo success(Object object){
        ResultVo resultVo = new ResultVo();
        resultVo.setData(object);
        resultVo.setCode(StatusCodeEnum.OK.getCode());
        resultVo.setMassage("成功");
        return resultVo;
    }

    public static ResultVo success(){
        return success(null);
    }

    public static ResultVo error(Integer code,String massage){
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMassage(massage);
        return resultVo;
    }
}
