package com.demo.wenda.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * create by: one
 * create time:2018/11/29 16:27
 * 描述：标签表操作
 */
@Mapper
public interface TagDao {
    String TABLE_NAME = "tag";
    String INSERT_FIELDS = "tag_name";

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{tagName})"})
    @Options(useGeneratedKeys=true, keyProperty="tagId", keyColumn="tag_id")//插入成功后马上返回，其自增的id
    Integer add(String tagName);

    @Select({"select tag_name from",TABLE_NAME,"where tag_id=#{id}"})
    String getNameById(int id);

    @Select({"select tag_id from",TABLE_NAME,"where tag_name=#{tagName}"})
    Integer getIdByName(String tagName);
}
