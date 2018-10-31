package com.demo.wenda.utils;

import java.util.UUID;

public class UUIUtil {

    /*
   得到一个UUID
   UUID：随机生成的一个128-bit的值（不可变，唯一标识）
    */
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
