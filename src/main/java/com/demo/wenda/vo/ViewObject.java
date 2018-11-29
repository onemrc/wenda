package com.demo.wenda.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ViewObject implements Serializable {


    private static final long serialVersionUID = 6609171029816513485L;

    private Map<String, Object> objs = new HashMap<>();

    public void set(String key, Object obj) {
        objs.put(key, obj);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
