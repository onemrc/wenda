package com.demo.wenda.service;

import com.demo.wenda.dao.TagDao;
import com.demo.wenda.utils.RedisKeyUtil;
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

    private final RedisService redisService;

    @Autowired
    public TagService(TagDao tagDao, RedisService redisService) {
        this.tagDao = tagDao;
        this.redisService = redisService;
    }

    public Integer addTag(String tagName) {
        return tagDao.add(tagName);
    }

    public String getNameById(int id) {
        return tagDao.getNameById(id);
    }

    public Integer getIdByName(String tagName) {
        return tagDao.getIdByName(tagName);
    }


    /**
     * 将标签下的问题id存进redis
     * @param questionId questionId
     * @param tagId tagId
     * @return
     */
    public boolean addQuestion(int questionId,int tagId){
        String key = RedisKeyUtil.getTagQuestionKey(tagId);

        return redisService.sadd(key,questionId+"") >0;
    }

}
