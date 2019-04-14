package com.xl.tmall.service;

import com.xl.tmall.pojo.Category;
import com.xl.tmall.utils.PageNavigator;

import java.util.List;

public interface CategoryService {

//    查询所有
    List<Category> findAll();

//    带分页条件的查询
    PageNavigator<Category> findAll(int start, int size, int navigatePages);

//    添加分类
    void save(Category category);

//    删除
    void delete(int id);

//    根据id查询
    Category findById(int id);

//    修改
    void update(Category category);

    void removeCategoryFromProduct(Category category);

    void removeCategoryFromProduct(List<Category> categories);
}
