package com.demo.wenda.service;

import com.demo.wenda.dao.CommentDao;
import com.demo.wenda.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    private final CommentDao commentDao;

    @Autowired
    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDao.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

    public int deleteCommentByEntity(int entityId, int entityType,int userId,int statusCode){
        return commentDao.deleteComment(entityId,entityType,userId,statusCode);
    }

    /**
     * 某用户是否在某实体下已有评论
     * @param entityId
     * @param entityType
     * @param userId
     * @return
     */
    public Integer queryIdExist(int entityId, int entityType,int userId){
        return commentDao.selectIdByEntityIdAndEntityType(entityId,entityType,userId);
    }

    public Integer getEntityIdByCommentId(int commentId){
        return commentDao.getEntityId(commentId);
    }

    public Comment getCommentById(int commentId){
        return commentDao.getCommentById(commentId);
    }


    public Long getUserAnswerCount(int userId){
        return commentDao.getUserAnswerCount(userId);
    }

    public String getContentById(int commentId){
        return commentDao.getContentById(commentId);
    }

    public Integer getEntityTypeById(int id){
        return commentDao.getEntityTypeById(id);
    }

    /**
     * 获取某问题最新评论时间
     *
     * @param questionId
     * @return
     */
    public String getQuestionLastCommentTime(int questionId) {
        List<String> allTime = commentDao.getQuestionLastCommentTime(questionId);
        if (allTime.size() > 0) {
            return commentDao.getQuestionLastCommentTime(questionId).get(0);
        }
        return null;
    }

}
