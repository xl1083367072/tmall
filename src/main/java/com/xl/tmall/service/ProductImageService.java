package com.xl.tmall.service;

import com.xl.tmall.pojo.OrderItem;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.ProductImage;

import java.util.List;

public interface ProductImageService {

//    单张商品图片类型
    static final String SINGLETYPE = "single";
//    商品细节图片类型
    static final String DETAILTYPE = "detail";

//    查询指定商品的所有单张类型图片
    List<ProductImage> listSingleTypeImage(Product product);
//    查询指定商品的所有细节类型图片
    List<ProductImage> listDetailTypeImage(Product product);

    ProductImage findById(int id);

    void save(ProductImage productImage);

    void delete(int id);
//  设置商品的代表图片
    void setFirstImage2Product(Product product);
//  设置多个商品的代表图片
    void setFirstImage2Products(List<Product> products);

    void setFirstImage2OrderItems(List<OrderItem> orderItems);

}
