package com.xl.tmall.compartor;

import com.xl.tmall.pojo.Product;

import java.util.Comparator;

//价格比较器,根据价格排序
public class ProductPriceCompartor implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPromotePrice() - p2.getPromotePrice());
    }
}
