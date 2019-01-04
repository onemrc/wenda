package com.demo.wenda.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Proof;
import com.demo.wenda.domain.User;
import com.demo.wenda.service.ProofService;
import com.demo.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    private final UserService userService;

    private final HostHolder hostHolder;

    private final ProofService proofService;

    @Autowired
    public PassportInterceptor(UserService userService, HostHolder hostHolder, ProofService proofService) {
        this.userService = userService;
        this.hostHolder = hostHolder;
        this.proofService = proofService;
    }

    /*
        在处理controller之前，调用preHandle
        */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从redis取出User
        User user = getUser(request);

        //放入hostHolder上下文中
        hostHolder.setUsers(user);

        //取出user对应的proof
        if (user != null) {
            Proof proof = proofService.getProofByUserId(user.getUserId());
            hostHolder.setPoof(proof);
        }

        return true;
    }


    /*

     */
    private User getUser(HttpServletRequest request) {
        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken)) {
            return null;
        }

        return userService.getByToken(cookieToken);
    }

    /*
    取cookie
     */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }


    /*
    清除处理后留下的信息
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        hostHolder.clear();
    }
}
