package com.demo.wenda.aspect;

import com.demo.wenda.interceptor.LoginRequiredInterceptor;
import com.demo.wenda.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;


    /*
    注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //访问所有页面都需要对用户的token进行验证
        registry.addInterceptor(passportInterceptor);
    }
}
