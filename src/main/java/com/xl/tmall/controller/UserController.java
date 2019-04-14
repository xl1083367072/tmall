package com.xl.tmall.controller;

import com.xl.tmall.pojo.User;
import com.xl.tmall.service.UserService;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public PageNavigator<User> findAll(@RequestParam(name = "start",defaultValue = "0")int start,
                                       @RequestParam(name = "size",defaultValue = "10")int size){
        start = start<0?0:start;
        PageNavigator<User> pageNavigator = userService.findAll(start, size, 5);
        return pageNavigator;
    }

}
