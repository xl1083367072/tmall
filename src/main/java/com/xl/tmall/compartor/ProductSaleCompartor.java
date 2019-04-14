package com.xl.tmall.compartor;

import com.xl.tmall.pojo.Product;

import java.util.Comparator;

//销量比较器,销量高的放在前面显示
public class ProductSaleCompartor implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount() - p1.getSaleCount();
    }
}
