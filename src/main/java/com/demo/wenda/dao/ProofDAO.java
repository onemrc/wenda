package com.demo.wenda.dao;

import com.demo.wenda.domain.Proof;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by: one
 * create time:2018/12/23 21:28
 * 描述：
 */
@Mapper
public interface ProofDAO {
    String TABLE_NAME = "proof";
    String INSERT_FIELDS = "proof_id,type,type_name,user_id";

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{id},#{type},#{typeName},#{userId})"})
    int addProof(Proof proof);

    @Select({"select * from", TABLE_NAME, "where user_id = #{userId}"})
    Proof getProofByUserId(int userId);
}
