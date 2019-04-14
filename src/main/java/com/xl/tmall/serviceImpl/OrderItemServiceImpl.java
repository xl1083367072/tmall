package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.OrderItemDao;
import com.xl.tmall.pojo.Order;
import com.xl.tmall.pojo.OrderItem;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.User;
import com.xl.tmall.service.OrderItemService;
import com.xl.tmall.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    ProductImageService productImageService;

    @Override
    public void fill(Order order) {
        List<OrderItem> orderItems = findByOrder(order);
        float totalPrice = 0;
        int totalNumber = 0;
        for (OrderItem orderItem:orderItems){
            totalPrice += orderItem.getNumber()*orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
            productImageService.setFirstImage2Product(orderItem.getProduct());
        }
        order.setTotalNumber(totalNumber);
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);
    }

    @Override
    public void fill(List<Order> orders) {
        for (Order order:orders){
            fill(order);
        }
    }

    @Override
    public List<OrderItem> findByOrder(Order order) {
        return orderItemDao.findByOrderOrderByIdDesc(order);
    }

//    返回商品销量
    @Override
    public int getSaleCount(Product product) {
        int saleCount = 0;
        List<OrderItem> orderItems = findByProduct(product);
        for (OrderItem orderItem:orderItems){
//            判断一下,只有从属订单且有确认收货日期才算销量
            if(orderItem.getOrder()!=null&&orderItem.getOrder().getConfirmDate()!=null){
                saleCount += orderItem.getNumber();
            }
        }
        return saleCount;
    }

    @Override
    public List<OrderItem> findByProduct(Product product) {
        return orderItemDao.findByProduct(product);
    }

//    查找还没有生成订单的订单项,即立即购买或者购物车中的订单项
    @Override
    public List<OrderItem> findByUser(User user) {
        return orderItemDao.findByUserAndOrderIsNull(user);
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }

    @Override
    public void save(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }

    @Override
    public OrderItem findById(int id) {
        return orderItemDao.getOne(id);
    }

    @Override
    public OrderItem findByUserAndAndProduct(User user, Product product) {
        return orderItemDao.findByUserAndAndProduct(user,product);
    }
}
