package com.demo.wenda.dao;

import com.demo.wenda.domain.Question;
import com.demo.wenda.enums.QuestionStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionDaoTest {

    @Autowired
    QuestionDao questionDao;

    @Test
    public void addQuestion() throws Exception {
        Question question = new Question();
        question.setTitle("如何看待英雄联盟 S8 四强赛 IG 3:0 淘汰 G2 杀入决赛？");
        question.setContent("S4 后中国队再次杀入决赛");
        question.setAnonymous(QuestionStatusEnum.IS_ANONYMOUS.getCode());

        Date date = new Date();
        date.setTime(date.getTime());
        question.setCreateTime(date);

        question.setUserId(1);
        question.setTagId(1);


       Assert.assertNotNull(questionDao.addQuestion(question));
    }

    @Test
    public void selectLastQuestions() throws Exception {

        Assert.assertNotNull(questionDao.selectLatestQuestions(1,0,10));

        System.out.print(questionDao.selectLatestQuestions(1,0,10));
    }

}