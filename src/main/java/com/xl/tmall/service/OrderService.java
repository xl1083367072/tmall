package com.xl.tmall.service;

import com.xl.tmall.pojo.Order;
import com.xl.tmall.pojo.OrderItem;
import com.xl.tmall.pojo.User;
import com.xl.tmall.utils.PageNavigator;

import java.util.List;

public interface OrderService {

    static final String WAITPAY = "waitPay";
    static final String WAITDELIVERY = "waitDelivery";
    static final String WAITCONFIRM = "waitConfirm";
    static final String WAITREVIEW = "waitReview";
    static final String FINISH = "finish";
    static final String DELETE = "delete";

    PageNavigator<Order> findAll(int start,int size,int navigatePages);

    Order findById(int id);

    void update(Order order);

    void removeOrderFromOrderItem(Order order);

    void removeOrderFromOrderItem(List<Order> orders);

    float save(Order order, List<OrderItem> orderItems);

    List<Order> findWithoutDelete(User user);

    List<Order> findNotDeleteFillOrderIetms(User user);

    void delete(int id);

    void caculateTotalPrice(Order order);

}
