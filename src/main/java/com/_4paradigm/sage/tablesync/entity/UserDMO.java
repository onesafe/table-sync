package com._4paradigm.sage.tablesync.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by wangyiping on 2020/3/11 6:24 PM.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDMO {
    private String id;
    private String username;
    private String password;
    private Integer types;
    private String phone;
    private String email;
    private String partner_id;
    private String status;
    private Date last_login_at;
    private String updated_by;
    private Date created_at;
    private Date updated_at;
    private String remark;
    private String portrait;
    private Integer editedByAdmin;
    private Integer post;
    private String description;
    private String name;
    private Integer department;
}
