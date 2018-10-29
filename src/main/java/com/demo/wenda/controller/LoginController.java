package com.demo.wenda.controller;

import com.demo.wenda.domain.User;
import com.demo.wenda.service.UserService;
import com.demo.wenda.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.validation.Valid;

@Controller
@RequestMapping(value = "/wenda")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/do_login")
    @ResponseBody
    public void login(@Valid LoginVO loginVo){
       User user = userService.queryUser(loginVo.getStr(),loginVo.getPassword());
       if (user != null){
           logger.info("用户登录成功：",user.getName());
       }else{
           logger.info("登录失败");
       }
    }
}
