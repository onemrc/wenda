package com.demo.wenda.dao;

import com.demo.wenda.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name,password,salt,sex,email,phone";


    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{name},#{password},#{salt},#{sex},#{email},#{phone})"})
    int addUser(User user);


    @Select({"select * from",TABLE_NAME,"where user_id = #{userId}"})
    User getById(Integer userId);


    User queryUser(@Param("str")  String str,@Param("password") String password);

    @Select({"select salt from",TABLE_NAME,"where name = #{str} OR email = #{str}"})
    String getSaltByStr(String str);

    @Select({"select * from",TABLE_NAME,"where phone = #{str} OR email = #{str}"})
    User selectUserPhoneOrEmail(String str);

    @Select({"select * from",TABLE_NAME,"where name = #{name}"})
    User selectUserByName(String name);

    @Select({"select name from",TABLE_NAME,"where user_id = #{id}"})
    String getUserNameById(Integer id);

    @Select({"select introduction from",TABLE_NAME,"where user_id = #{id}"})
    String getIntroductionById(Integer id);

    @Select({"select user_id from",TABLE_NAME,"limit #{currIndex} , #{pageSize}"})
    List<Integer> getUserIdBySize(Integer currIndex,Integer pageSize);

    @Select({"select head_url from",TABLE_NAME,"where user_id = #{id}"})
    String getHeadUrlById(int id);

}
