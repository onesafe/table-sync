package com._4paradigm.sage.tablesync.mapper;

import com._4paradigm.sage.tablesync.entity.UserDMO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by wangyiping on 2020/3/11 6:24 PM.
 */
@Mapper
@Repository
public interface UserMapper {

    @Insert(
            "insert into " +
            "user " +

            "(" +
                    "id, username, password, types, phone, " +
                    "email, partner_id, status, last_login_at, updated_by, " +
                    "created_at, updated_at, remark, portrait, editedByAdmin, " +
                    "post, description, name, department" +
            ") " +

            "values " +

            "(" +
            "        #{userDMO.id}, #{userDMO.username}, #{userDMO.password}, #{userDMO.types}, #{userDMO.phone}, " +
                    "#{userDMO.email}, #{userDMO.partner_id}, #{userDMO.status}, #{userDMO.last_login_at}, #{userDMO.updated_by}, " +
                    "#{userDMO.created_at}, #{userDMO.updated_at}, #{userDMO.remark}, #{userDMO.portrait}, #{userDMO.editedByAdmin}, " +
                    "#{userDMO.post}, #{userDMO.description}, #{userDMO.name}, #{userDMO.department}" +
            ")"
    )
    void insert(@Param("userDMO") UserDMO userDMO);


    @Delete("delete from user where id=#{id}")
    void delete(@Param("id") String id);


    @Update(
            "update user " +
            "set " +

                    "username = #{userDMO.username}, password = #{userDMO.password}, types = #{userDMO.types}, " +
                    "phone = #{userDMO.phone}, email = #{userDMO.email},  partner_id = #{userDMO.partner_id}, " +
                    "status = #{userDMO.status}, last_login_at = #{userDMO.last_login_at}, updated_by = #{userDMO.updated_by}, " +
                    "created_at = #{userDMO.created_at}, updated_at = #{userDMO.updated_at},  remark = #{userDMO.remark}, " +
                    "portrait = #{userDMO.portrait}, editedByAdmin = #{userDMO.editedByAdmin}, post = #{userDMO.post}, " +
                    "description = #{userDMO.description}, name = #{userDMO.name},  department = #{userDMO.department} " +

            "where id = #{userDMO.id}"
    )
    void update(@Param("userDMO") UserDMO userDMO);
}
