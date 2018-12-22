package com.demo.wenda.service;

import com.demo.wenda.dao.FindDAO;
import com.demo.wenda.domain.Find;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * create by: one
 * create time:2018/12/22 16:45
 * 描述：
 */
@Service
public class FindService {
    @Autowired
    FindDAO findDAO;

    public List<Find> getUserFeeds(int maxId, Set<String> userIds, int count) {
        return findDAO.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Find feed) {
        findDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Find getById(int id) {
        return findDAO.getFeedById(id);
    }
}
