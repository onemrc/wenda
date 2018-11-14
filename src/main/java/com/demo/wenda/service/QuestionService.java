package com.demo.wenda.service;

import com.demo.wenda.dao.QuestionDao;
import com.demo.wenda.domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    private QuestionDao questionDao;


    private SensitiveService sensitiveService;

    @Autowired
    public QuestionService(QuestionDao questionDao, SensitiveService sensitiveService) {
        this.questionDao = questionDao;
        this.sensitiveService = sensitiveService;
    }

    public List<Question> getUserLatestQuestions(int userId, int offset, int limit){
        return questionDao.selectUserLatestQuestions(userId,offset,limit);
    }

    public List<Question> getLatestQuestions(){
        return questionDao.selectLatestQuestions();
    }

    public int addQuestion(Question question){
        //html标签过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        return questionDao.addQuestion(question) > 0 ? question.getQuestionId() : 0;
    }

    public Question getById(int id){
        return questionDao.getById(id);
    }

}
