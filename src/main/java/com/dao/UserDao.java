package com.dao;

import com.beans.UserInfo;

public interface UserDao {
    int addUser(UserInfo user);

    UserInfo getUserByName(String userName);

    int delUser(String username);
}
