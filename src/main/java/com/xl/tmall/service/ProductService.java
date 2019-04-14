package com.xl.tmall.service;

import com.xl.tmall.pojo.Category;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.utils.PageNavigator;

import java.util.List;

public interface ProductService {

    PageNavigator<Product> findAll(int cid,int start,int size,int navigatePages);

    Product findById(int id);

    void save(Product product);

    void delete(int id);

    void update(Product product);

    void fill(Category category);

    void fill(List<Category> categories);

    void fillByRow(List<Category> categories);

    List<Product> findByCategory(Category category);

    void setSaleAndReviewCount(Product product);

    void setSaleAndReviewCount(List<Product> products);

    List<Product> findByKeyword(String keyword,int start,int size);

}
