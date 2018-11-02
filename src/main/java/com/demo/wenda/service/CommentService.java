package com.demo.wenda.service;

import com.demo.wenda.dao.CommentDao;
import com.demo.wenda.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentDao commentDao;

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }
}
