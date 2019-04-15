package com.xl.tmall.dao;

import com.xl.tmall.pojo.Order;
import com.xl.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Order,Integer> {
//    查询指定用户的不是某种状态的订单,主要用来排除掉delete状态订单
    List<Order> findByUserAndStatusNotOrderByIdDesc(User user,String status);
}
