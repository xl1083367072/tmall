package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.UserDao;
import com.xl.tmall.pojo.User;
import com.xl.tmall.service.UserService;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public PageNavigator<User> findAll(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<User> page = userDao.findAll(pageable);
        return new PageNavigator<>(page,navigatePages);
    }

    @Override
    public User findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public boolean isExist(String name) {
        User user = findByName(name);
        return user!=null;
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User findByNameAndPassword(String name, String password) {
        return userDao.findByNameAndPassword(name,password);
    }
}
