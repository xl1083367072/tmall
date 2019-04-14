package com.xl.tmall.service;

import com.xl.tmall.pojo.User;
import com.xl.tmall.utils.PageNavigator;

public interface UserService {

    PageNavigator<User> findAll(int start, int size, int navigatePages);

    User findByName(String name);

    boolean isExist(String name);

    void save(User user);

    User findByNameAndPassword(String name,String password);

}
