package com.demo.wenda.dao;

import com.demo.wenda.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name,password,salt,sex";


    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{name},#{password},#{salt},#{sex})"})
    int addUser(User user);

    @Select({"select * from",TABLE_NAME,"where user_id = #{userId}"})
    User getById(Integer userId);


    User queryUser(@Param("str")  String str,@Param("password") String password);
}
