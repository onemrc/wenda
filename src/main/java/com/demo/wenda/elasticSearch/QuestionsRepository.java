package com.demo.wenda.elasticSearch;


import com.demo.wenda.domain.Question;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * create by: one
 * create time:2019/1/19 20:51
 * 描述：
 */
@Component
public interface QuestionsRepository extends ElasticsearchRepository<Question, Integer> {
    @Override
    Iterable<Question> search(QueryBuilder queryBuilder);

    @Override
    Optional<Question> findById(Integer integer);

}
