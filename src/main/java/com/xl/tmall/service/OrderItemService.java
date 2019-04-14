package com.xl.tmall.service;

import com.xl.tmall.pojo.Order;
import com.xl.tmall.pojo.OrderItem;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.User;

import java.util.List;

public interface OrderItemService {

    void fill(Order order);

    void fill(List<Order> orders);

    List<OrderItem> findByOrder(Order order);

    int getSaleCount(Product product);

    List<OrderItem> findByProduct(Product product);

    List<OrderItem> findByUser(User user);

    void update(OrderItem orderItem);

    void save(OrderItem orderItem);

    OrderItem findById(int id);

    OrderItem findByUserAndAndProduct(User user,Product product);

}
