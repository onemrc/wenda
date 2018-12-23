package com.demo.wenda.service;

import com.demo.wenda.dao.UserDao;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.StatusCodeEnum;
import com.demo.wenda.redis.UserKey;
import com.demo.wenda.utils.MD5Util;
import com.demo.wenda.utils.UUIUtil;
import com.demo.wenda.utils.ValidatorUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    public static final String COOKIE_NAME_TOKEN = "token";


    private final UserDao userDao;

    private final RedisService redisService;

    @Autowired
    public UserService(UserDao userDao, RedisService redisService) {
        this.userDao = userDao;
        this.redisService = redisService;
    }

    public User getById(Integer id) {
        return userDao.getById(id);
    }

    public int addUser(User user) {
        return userDao.addUser(user);
    }

    public Boolean login(HttpServletResponse response,String str, String password) {
        String salt = userDao.getSaltByStr(str);
        String real_pass = MD5Util.md5(password + salt);

        //验证
        User user = userDao.queryUser(str, real_pass);

        if (user == null){
            return false;
        }


        addCookie(response,user);
        return true;
    }

    /*
    将User存进redis，并生成一个cookie
     */
    private void addCookie(HttpServletResponse response,User user){
        //生成一个token
        String token = UUIUtil.uuid();

        //存入缓存
        redisService.set(UserKey.token,token,user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);

        //Cookie有效期 == redis有效期
//        cookie.setMaxAge(UserKey.token.getExpireDate());


        cookie.setPath("/");

        response.addCookie(cookie);
    }

    /*
    将User从redis中取出
     */
    public User getByToken(String token){
        return redisService.get(UserKey.token,token,User.class);
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
                map.put("code", StatusCodeEnum.EMAIL_EXIST.getCode()+"");
                return map;
            } else if (ValidatorUtil.isMobile(str)) {
                map.put("msg", "该手机号已被注册");
                map.put("code", StatusCodeEnum.PHONE_EXIST.getCode()+"");
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
        user.setIntroduction("这个人很赖，没有个人介绍..");
        userDao.addUser(user);

        return map;
    }


    public User getUserByName(String name){
        return userDao.selectUserByName(name);
    }

    public String getUserNameById(Integer id){
        return userDao.getUserNameById(id);
    }

    public String getIntroductionById(Integer id){
        return userDao.getIntroductionById(id);
    }

    /**
     * 用户名或邮箱是否已存在
     * @param str
     * @return
     */
    public boolean strIsExist(String str){
        return userDao.selectUserPhoneOrEmail(str) ==null;
    }


    public List<Integer> getUserIdBySize(Integer currIndex,Integer pageSize){
        return userDao.getUserIdBySize(currIndex,pageSize);
    }

    /**
     * 获取用户头像路径
     * @param userId
     * @return
     */
    public String getUserHeadUrl(int userId){
        return userDao.getHeadUrlById(userId);
    }


    /**
     * 用户登出
     * @param response
     * @return
     */
    public Boolean loginOut(HttpServletResponse response){
        try{
            Cookie cookie = new Cookie("token",null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 修改用户名
     *
     * @param id
     * @param name
     * @return
     */
    public Integer editName(int id, String name) {
        return userDao.editName(id, name);
    }

    /**
     * 验证密码
     *
     * @param str  用户名或邮箱
     * @param pass
     * @return
     */
    public Boolean verifyPass(String str, String pass) {
        String salt = userDao.getSaltByStr(str);
        String real_pass = MD5Util.md5(pass + salt);

        //验证
        User user = userDao.queryUser(str, real_pass);

        return user != null;
    }

    /**
     * 修改密码
     *
     * @param newPass
     * @param userId
     * @return
     */
    public Integer editPass(String newPass, int userId) {
        String salt = userDao.getSaltById(userId);
        String real_pass = MD5Util.md5(newPass + salt);
        return userDao.editPass(userId, real_pass);
    }
}
