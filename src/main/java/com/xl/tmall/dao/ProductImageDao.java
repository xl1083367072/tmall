package com.xl.tmall.dao;

import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDao extends JpaRepository<ProductImage,Integer> {
    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product,String type);
}
