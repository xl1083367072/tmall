package com.xl.tmall.dao;

import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.Property;
import com.xl.tmall.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueDao extends JpaRepository<PropertyValue,Integer> {
    List<PropertyValue> findByProductOrderByIdDesc(Product product);
    PropertyValue findByProductAndProperty(Product product, Property property);
    void deleteByProductOrProperty(Product product,Property property);
}
