package com.demo.wenda.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConverterUtil {
    /*
    将一个bean转化为String
     */
    public static  <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return value + "";
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == Long.class) {
            return value + "";
        } else {
            return JSON.toJSONString(value);
        }
    }

  /*
    将一个String转化为bean
     */
    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }


    public static String getJSONString(int code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        return jsonObject.toString();
    }

    public static String getJSONString(int code,String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return jsonObject.toString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    public static String getJSONList(List list){
        JSONObject json = new JSONObject();
        json.put("list",list);
        return json.toJSONString();
    }

}
