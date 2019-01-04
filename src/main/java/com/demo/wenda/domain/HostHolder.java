package com.demo.wenda.domain;



import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    /**
     * 线程本地变量
     * 每条线程都有自己的一个变量
     */
    private static ThreadLocal<User> users = new ThreadLocal<>();//存用户信息

    private static ThreadLocal<Proof> proof = new ThreadLocal<>();//存身份信息


    public  User getUsers() {
        return users.get();
    }

    public  void setUsers(User user) {
        users.set(user);
    }

    public void setPoof(Proof poof) {
        proof.set(poof);
    }

    public Proof getPoof() {
        return proof.get();
    }

    public void clear() {
        users.remove();
    }
}
