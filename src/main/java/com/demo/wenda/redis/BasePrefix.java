package com.demo.wenda.redis;

public abstract class BasePrefix implements KeyPrefix{//抽象类，不能实例化

    private int expireDate;

    private String prefix;

    public BasePrefix(int expireDate, String prefix) {
        this.expireDate = expireDate;
        this.prefix = prefix;
    }

    @Override
    public int getExpireDate() {
        return expireDate;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
