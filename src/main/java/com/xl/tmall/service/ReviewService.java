package com.xl.tmall.service;

import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.Review;

import java.util.List;

public interface ReviewService {

    List<Review> findByProduct(Product product);

    int getReviewCount(Product product);

    void save(Review review);

}
