package com.demo.wenda.service;

import com.demo.wenda.dao.QuestionDao;
import com.demo.wenda.domain.Question;
import com.demo.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class QuestionService {

    private final QuestionDao questionDao;


    private final SensitiveService sensitiveService;

    private final RedisService redisService;

    @Autowired
    public QuestionService(QuestionDao questionDao, SensitiveService sensitiveService, RedisService redisService) {
        this.questionDao = questionDao;
        this.sensitiveService = sensitiveService;
        this.redisService = redisService;
    }

    public List<Question> getUserLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectUserLatestQuestions(userId, offset, limit);
    }

    public List<Question> getLatestQuestions() {
        return questionDao.selectLatestQuestions();
    }

    /**
     * 获取热榜 问题id
     *
     * @return
     */
    public List<Question> getHotQuestion() {

        //前100
        Set<String> questions = redisService.zrevrange(RedisKeyUtil.getHotList(), 0, 100);

        List list = new ArrayList();
        for (String questionId : questions) {
            list.add(questionDao.getById(Integer.valueOf(questionId)));
        }

        return list;
    }

    public int addQuestion(Question question) {
        //html标签过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        Date date = new Date();
        question.setCreateTime(date);

        question.setAnonymous(1);

        return questionDao.addQuestion(question) > 0 ? question.getQuestionId() : 0;
    }

    public Question getById(int id) {
        return questionDao.getById(id);
    }


    /**
     * 将问题拥有的标签存进redis
     *
     * @param questionId questionId
     * @param tagId      tagId
     * @return
     */
    public boolean addTag(int questionId, int tagId) {

        String key = RedisKeyUtil.getQuestionTagKey(questionId);

        return redisService.sadd(key, tagId + "") > 0;

    }

    /**
     * 获取问题的来浏览数
     * @param questionId
     * @return
     */
    public Long getLookCount(int questionId){
        String key = RedisKeyUtil.getQuestionLook(questionId);
        return Long.valueOf( redisService.get(key));
    }

    /**
     * 获取问题的全部标签
     * @param questionId
     * @return
     */
    public Set<String> getTags(int questionId){
        String key = RedisKeyUtil.getQuestionTagKey(questionId);
        return redisService.smembers(key);
    }

    /**
     * 获取某用户的提问数
     * @param userId userId
     * @return
     */
    public Long getUserQuestinCount(Integer userId){
        return questionDao.getUserQuestionCount(userId);
    }

    /**
     * 问题评论数 +1
     * @param questionId
     * @return
     */
    public Long incrCommentCount(Integer questionId){
        return questionDao.incrCommentCount(questionId);
    }

    /**
     * 获取问题创建时间
     *
     * @param questionId
     * @return
     */
    public String getCreateTime(int questionId) {
        return questionDao.getTime(questionId);
    }

}
