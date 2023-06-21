package com.dao.impl;

import com.beans.UserInfo;
import com.dao.UserDao;
import com.jdbc.DBUtil;

public class UserDaoImpl implements UserDao {
    public int addUser(UserInfo user) {
        String sql="insert into userInfo (userName,password,phone) values (?,?,?)";
        return DBUtil.update(sql, user.getUserName(),user.getPassword(),user.getPhone());
    }

    //根据用户名查询用户
    public UserInfo getUserByName(String userName) {
        String sql="select * from userInfo where userName=? limit 1";
        return DBUtil.getSingleObj(sql, UserInfo.class, userName);
    }

    //根据用户名 删除用户
    public int delUser(String userName) {
        return DBUtil.update("delete from userInfo where userName=?", userName);
    }

    public UserInfo login(String userName, String password) {
        String sql="select * from userInfo where userName=? and password=?" ;
        return DBUtil.getSingleObj(sql, UserInfo.class, userName,password);
    }
}
