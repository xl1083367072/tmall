package com.xl.tmall.compartor;

import com.xl.tmall.pojo.Product;

import java.util.Comparator;

//人气比较器,评价高的放在前面显示
public class ProductReviewCompartor implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount() - p1.getReviewCount();
    }
}
