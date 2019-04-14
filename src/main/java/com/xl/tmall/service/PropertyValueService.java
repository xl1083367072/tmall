package com.xl.tmall.service;

import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.Property;
import com.xl.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {

//    修改属性值
    void update(PropertyValue propertyValue);
//  初始化属性值
    void init(Product product);
//    查询商品的所有属性值
    List<PropertyValue> findByProductOrderByIdDesc(Product product);
//  查询商品的指定属性的属性值
    PropertyValue findByProductAndProperty(Product product, Property property);
//  删除指定属性的属性值
    void deleteByProductOrProperty(Product product,Property property);
}
