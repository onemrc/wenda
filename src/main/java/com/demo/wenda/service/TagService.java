package com.demo.wenda.service;

import com.demo.wenda.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by: one
 * create time:2018/11/29 16:31
 * 描述：标签 服务
 */
@Service
public class TagService {


   private final TagDao tagDao;

   @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public Integer addTag(String tagName){
        return tagDao.add(tagName);
    }

    public String getNameById(int id){
       return tagDao.getNameById(id);
    }

    public Integer getIdByName(String tagName){
       return tagDao.getIdByName(tagName);
    }
}
