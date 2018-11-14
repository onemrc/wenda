//package com.demo.wenda.aspect;
//
//import com.demo.wenda.domain.HostHolder;
//import com.demo.wenda.interceptor.LoginRequiredInterceptor;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Aspect
//@Component
//public class WebAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebAspect.class);
//
//    private HostHolder hostHolder;
//
//    private LoginRequiredInterceptor loginRequiredInterceptor;
//
//    @Autowired
//    public WebAspect(HostHolder hostHolder, LoginRequiredInterceptor loginRequiredInterceptor) {
//        this.hostHolder = hostHolder;
//        this.loginRequiredInterceptor = loginRequiredInterceptor;
//    }
//
//    @Pointcut("execution(public * com.demo.wenda.controller..*.*(..))")
//    public void webLoginRequired() {
//    }
//
//    @Before("webLoginRequired()")
//    public void doBefore() {
//
//    }
//}
