package com.xl.tmall.dao;

import com.xl.tmall.pojo.Order;
import com.xl.tmall.pojo.OrderItem;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDao extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrderOrderByIdDesc(Order order);
    List<OrderItem> findByProduct(Product product);
    List<OrderItem> findByOrUserAndOrderIsNull(User user);
}
