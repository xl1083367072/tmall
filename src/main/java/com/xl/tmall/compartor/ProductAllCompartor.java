package com.xl.tmall.compartor;

import com.xl.tmall.pojo.Product;

import java.util.Comparator;

//综合比较器,将销量*评价更高的放在前面显示
public class ProductAllCompartor implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()*p2.getReviewCount() - p1.getSaleCount()*p1.getSaleCount();
    }

}
