package com.demo.wenda.controller;

import com.demo.wenda.service.UserService;
import com.demo.wenda.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * 登录注册
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/do_login")
    public String doLogin(HttpServletResponse response,
                          @RequestParam("str") String str,
                          @RequestParam("password") String password) {

        boolean result = userService.login(response, str, password);
        if (result) {
            logger.info("用户登录成功");
        } else {
            logger.info("登录失败");
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("str") String str,
                      @RequestParam("password") String password) {

        try {
            Map<String, String> map = userService.register(str, password);
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            //注册成功，跳到首页
            logger.info("用户注册成功:{}",str);
            return "redirect:/";
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("注册异常:{}", e.getMessage());
            return "login";
        }
    }

    @RequestMapping(value = "/to_login")
    public String to_login() {
        return "login";
    }



}
