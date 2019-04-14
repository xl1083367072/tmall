package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.ReviewDao;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.Review;
import com.xl.tmall.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewDao reviewDao;

//    返回商品的所有评价
    @Override
    public List<Review> findByProduct(Product product) {
        return reviewDao.findByProductOrderByIdDesc(product);
    }

//    返回商品的评价数量
    @Override
    public int getReviewCount(Product product) {
        return reviewDao.countByProduct(product);
    }

//    添加评价

    @Override
    public void save(Review review) {
        reviewDao.save(review);
    }
}
