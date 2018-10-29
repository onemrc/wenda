package com.demo.wenda.service;

import com.demo.wenda.dao.UserDao;
import com.demo.wenda.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getById(Integer id){
        return userDao.getById(id);
    }

    public int addUser(User user){
        return userDao.addUser(user);
    }

    public User queryUser( String str, String password){
        return userDao.queryUser(str,password);
    }
}
