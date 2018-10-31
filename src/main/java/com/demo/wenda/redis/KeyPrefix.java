package com.demo.wenda.redis;

public interface KeyPrefix {
    //    到期时间
    int getExpireDate();

    //    获取前缀
    String getPrefix();
}
