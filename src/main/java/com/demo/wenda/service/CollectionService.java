package com.demo.wenda.service;

import com.demo.wenda.dao.CollectionDAO;
import com.demo.wenda.domain.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * create by: one
 * create time:2018/12/1 17:48
 * 描述：收藏 服务
 */
@Service
public class CollectionService {
    @Autowired
    CollectionDAO collectionDAO;

    /**
     * 某用户收藏一个实体
     * @param userId 用户id
     * @param entityId 实体id
     * @param entityType 实体类型
     * @return 成功返回true
     */
    public boolean add(int userId,int entityId,int entityType){
        Collections collection = new Collections();
        collection.setUserId(userId);
        collection.setEntityId(entityId);
        collection.setEntityType(entityType);

        Date date = new Date();
        collection.setCreateDate(date);

        return collectionDAO.addComment(collection)!=null;
    }

    /**
     * 某用户删除一个实体
     * @param userId 用户id
     * @param entityId 实体id
     * @param entityType 实体类型
     * @return
     */
    public boolean remove(int userId,int entityId,int entityType){
        Collections collection = new Collections();
        collection.setUserId(userId);
        collection.setEntityId(entityId);
        collection.setEntityType(entityType);

        return collectionDAO.addComment(collection)!=null;
    }

    /**
     *
     * @param userId
     * @return 某用户收藏的实体数
     */
    public Integer getUserCollectionCount(int userId){
        return collectionDAO.getUserCollectionCount(userId);
    }
}
