package com._4paradigm.sage.tablesync.service;

import com._4paradigm.sage.tablesync.entity.UserDMO;
import com._4paradigm.sage.tablesync.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by wangyiping on 2020/3/11 6:24 PM.
 */
@Repository
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void insert(UserDMO userDMO) {
        userMapper.insert(userDMO);
    }


    public void delete(String id) {
        userMapper.delete(id);
    }


    public void update(UserDMO userDMO) {
        userMapper.update(userDMO);
    }

}
