package com.xl.tmall.service;

import com.xl.tmall.pojo.Category;
import com.xl.tmall.pojo.Property;
import com.xl.tmall.utils.PageNavigator;

import java.util.List;

public interface PropertyService {

//    根据id查询
    Property findById(int id);

//    添加
    void save(Property property);

//    删除
    void delete(int id);

//    修改
    void update(Property property);

//    查询所有
    PageNavigator<Property> findAll(int cid,int start,int size,int navigatePages);

//    通过分类的所有属性
    List<Property> findByCategory(Category category);
}
