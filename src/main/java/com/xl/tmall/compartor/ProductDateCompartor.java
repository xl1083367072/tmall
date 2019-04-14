package com.xl.tmall.compartor;

import com.xl.tmall.pojo.Product;

import java.util.Comparator;

//日期比较器,将创建日期更晚的放在前面展示
public class ProductDateCompartor implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getCreateDate().compareTo(p1.getCreateDate());
    }
}
