package com.demo.wenda.dao;

import com.demo.wenda.controller.IndexController;
import com.demo.wenda.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);

    @Autowired
    UserDao userDao;

    @Test
    public void addUser() throws Exception {
        User user = new User();

        user.setName("陈一");
        user.setPassword("123456");
        user.setSalt("123");
        user.setSex(1);

        Assert.assertNotNull(userDao.addUser(user));

    }

    @Test
    public void getUserById() throws Exception {
        User user = userDao.getById(1);

        Assert.assertNotNull(user);

//        logger.info("user:",user);
    }

    @Test
    public void queryUser() throws Exception {
        User userByPhone = userDao.queryUser("10086","123456");

        User userByName = userDao.queryUser("陈一","123456");

        User userByEmail = userDao.queryUser("11@qq.com","123456");

        Assert.assertNotNull(userByPhone);
        Assert.assertNotNull(userByName);
        Assert.assertNotNull(userByEmail);
    }
}