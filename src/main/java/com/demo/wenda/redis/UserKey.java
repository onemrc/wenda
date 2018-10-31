package com.demo.wenda.redis;

public class UserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24*2;//token过期时间

    public UserKey(int expireDate, String prefix) {
        super(expireDate, prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE,"tk");
}
