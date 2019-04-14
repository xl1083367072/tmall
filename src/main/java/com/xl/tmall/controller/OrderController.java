package com.xl.tmall.controller;

import com.xl.tmall.pojo.Order;
import com.xl.tmall.service.OrderItemService;
import com.xl.tmall.service.OrderService;
import com.xl.tmall.utils.PageNavigator;
import com.xl.tmall.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/orders")
    public PageNavigator<Order> findAll(@RequestParam(name = "start",defaultValue = "0")int start,
                                        @RequestParam(name = "size",defaultValue = "10")int size){
        start = start<0?0:start;
        PageNavigator<Order> pageNavigator = orderService.findAll(start, size, 5);
//        给所有订单填入订单项信息
        orderItemService.fill(pageNavigator.getContent());
//         给所有订单的订单项解除订单信息,使每一个订单单方面关联订单项,否则会无限循环,加jackson的注解会使redis出现bug
        orderService.removeOrderFromOrderItem(pageNavigator.getContent());
        return pageNavigator;
    }

//    改变订单状态为已发货待确认
    @PutMapping("/orders/{id}")
    public Result update(@PathVariable("id")int id){
        Order order = orderService.findById(id);
//        设置订单发货日期
        order.setDeliveryDate(new Date());
//        设置订单状态为待确认
        order.setStatus(OrderService.WAITCONFIRM);
        orderService.update(order);
        return Result.success();
    }

}
