package com.demo.wenda.utils;

import com.alibaba.druid.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");//以1开始，后面有10个数字

    private static final Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");//名称@域名

    public static boolean isMobile(String str){
        if (StringUtils.isEmpty(str)){
            return false;
        }

        Matcher m = mobile_pattern.matcher(str);
        return m.matches();
    }

    public static boolean isEmail(String str){
        Matcher m =email_pattern.matcher(str);
        return m.matches();
    }


//    public static void main(String[] args) {
//        System.out.println(isEmail("177qq.com"));
//    }
}
