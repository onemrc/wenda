package com.demo.wenda.dao;

import com.demo.wenda.domain.Find;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * create by: one
 * create time:2018/12/22 16:43
 * 描述：TODO
 */
@Mapper
public interface FindDAO {

    String TABLE_NAME = " find ";
    String INSERT_FIELDS = " user_id, data, create_date, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{data},#{createDate},#{type})"})
    int addFeed(Find feed);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Find getFeedById(int id);

    List<Find> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") Set<String> userIds,
                               @Param("count") int count);
}
