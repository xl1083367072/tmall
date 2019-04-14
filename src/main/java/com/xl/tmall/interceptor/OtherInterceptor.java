package com.xl.tmall.interceptor;

import com.xl.tmall.pojo.Category;
import com.xl.tmall.pojo.OrderItem;
import com.xl.tmall.pojo.User;
import com.xl.tmall.service.CategoryService;
import com.xl.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherInterceptor implements HandlerInterceptor {

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    CategoryService categoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

//    handle执行完成后执行,主要用于处理model对象
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        String contextPath = request.getServletContext().getContextPath();
        User user = (User) session.getAttribute("user");
        int totalNumber = 0;
        if(user!=null){
//            若用户不为空,计算出该用户购物车中商品数量
            List<OrderItem> orderItems = orderItemService.findByUser(user);
            for (OrderItem orderItem:orderItems){
                totalNumber += orderItem.getNumber();
            }
        }
        List<Category> categories = categoryService.findAll();
//        将所有分类放入ServletContext上下文,用于搜索框下面分类显示
        request.getServletContext().setAttribute("categoriesBelowSearch",categories);
//        将根路径放入ServletContext上下文,用于跳转到首页
        request.getServletContext().setAttribute("contextPath",contextPath);
//        将用户购物车中商品数量放入session域,用于显示用户购物车中商品数量
        session.setAttribute("cartTotalItemNumber",totalNumber);
    }
}
