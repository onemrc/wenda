package com.demo.wenda.service;

import com.demo.wenda.dao.UserDao;
import com.demo.wenda.domain.User;
import com.demo.wenda.utils.MD5Util;
import com.demo.wenda.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getById(Integer id) {
        return userDao.getById(id);
    }

    public int addUser(User user) {
        return userDao.addUser(user);
    }

    public User login(String str, String password) {
        String salt = userDao.getSaltByStr(str);
        String real_pass = MD5Util.md5(password + salt);
        return userDao.queryUser(str, real_pass);
    }

    public Map<String, String> register(String str, String password) {
        Map<String, String> map = new HashMap<>();

        //传进来的不是手机号也不是邮箱
        if (!ValidatorUtil.isMobile(str)) {
            if (!ValidatorUtil.isEmail(str)) {
                map.put("msg", "格式错误，必须为手机号或邮箱账号");
                return map;
            }
        }

        User user = userDao.selectUserPhoneOrEmail(str);
        if (user != null) {
            if (ValidatorUtil.isEmail(str)) {
                map.put("msg", "该邮箱已被注册");
                return map;
            } else if (ValidatorUtil.isMobile(str)) {
                map.put("msg", "该手机号已被注册");
                return map;
            }
        }

        user = new User();
        user.setName("用户"+str);
        if (ValidatorUtil.isEmail(str)){
            user.setEmail(str);
        }
        if (ValidatorUtil.isMobile(str)){
            user.setPhone(Long.valueOf(str));
        }

        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl("xx");
        user.setPassword(MD5Util.md5(password + user.getSalt()));
        userDao.addUser(user);

        return map;
    }
}
