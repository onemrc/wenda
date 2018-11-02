package com.demo.wenda.interceptor;

import com.demo.wenda.domain.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{

    @Autowired
    HostHolder hostHolder;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (hostHolder.getUsers() == null){
            //用户没登录
            //跳到登录页面，并携带当前页面参数
            response.sendRedirect("/reg?next=" + request.getRequestURI());
        }
        return true;
    }
}
