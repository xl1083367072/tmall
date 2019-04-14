package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.OrderDao;
import com.xl.tmall.pojo.Order;
import com.xl.tmall.pojo.OrderItem;
import com.xl.tmall.service.OrderItemService;
import com.xl.tmall.service.OrderService;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemService orderItemService;

    @Override
    public PageNavigator<Order> findAll(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Order> page = orderDao.findAll(pageable);
        return new PageNavigator<>(page,navigatePages);
    }

    @Override
    public Order findById(int id) {
        return orderDao.getOne(id);
    }

    @Override
    public void update(Order order) {
        orderDao.save(order);
    }

//    因为order对象中有orderitem对象,orderitem对象中又有order对象,序列化时会无限递归,这里吧orderitem的order设为空
    @Override
    public void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem:orderItems){
//            这里不能加事务了,否则数据库中的oid会清空
            orderItem.setOrder(null);
        }
    }

    @Override
    public void removeOrderFromOrderItem(List<Order> orders) {
        for (Order order:orders){
            removeOrderFromOrderItem(order);
        }
    }
}
