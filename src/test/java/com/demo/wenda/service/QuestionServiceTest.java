package com.demo.wenda.service;

import com.demo.wenda.controller.CommentController;
import com.demo.wenda.domain.Question;
import com.demo.wenda.enums.QuestionStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceTest.class);

    @Autowired
    QuestionService questionService;


    @Test
    public void getLatestQuestions() {
        List<Question> questionList = questionService.getLatestQuestions();

        Assert.assertNotNull(questionList);
    }

    @Test
    public void addQuestionWithSensitive() {
        Question question = new Question();
        question.setTitle("Title -Sensitive");
        question.setContent("嫖娼\n" +
                "赌博\n" +
                "情赌\n" +
                "色情");
        question.setAnonymous(QuestionStatusEnum.IS_ANONYMOUS.getCode());

        Timestamp time = new Timestamp(System.currentTimeMillis());
        question.setCreateTime(time);

        question.setUserId(1);
        question.setTagId(1);

        question.setLookCount(0);
        question.setCommentCount(0);

        Assert.assertNotEquals(0,questionService.addQuestion(question));
    }

    @Test
    public void addQuestions() {
        for (int i=0;i<1;i++){
            Question question = new Question();
            question.setTitle("Title -"+i);

            question.setContent("This is Content -- "+i);

            question.setAnonymous(QuestionStatusEnum.IS_ANONYMOUS.getCode());

            Timestamp time = new Timestamp(System.currentTimeMillis());
            question.setCreateTime(time);

            question.setAnonymous(QuestionStatusEnum.IS_ANONYMOUS.getCode());
            question.setUserId(1);
            question.setTagId(1);
            question.setLookCount(0);
            question.setCommentCount(0);

            Assert.assertNotEquals(0,questionService.addQuestion(question));
        }

    }

}