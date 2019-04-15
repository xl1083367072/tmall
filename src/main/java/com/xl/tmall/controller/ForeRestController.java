package com.xl.tmall.controller;

import com.xl.tmall.compartor.*;
import com.xl.tmall.pojo.*;
import com.xl.tmall.service.*;
import com.xl.tmall.utils.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ForeRestController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderService orderService;

    @GetMapping("/forehome")
    public List<Category> forehome(){
        List<Category> categories = categoryService.findAll();
        productService.fill(categories);
        productService.fillByRow(categories);
        categoryService.removeCategoryFromProduct(categories);
        return categories;
    }

    @PostMapping("/foreregister")
    public Result register(@RequestBody User user){
        String name = user.getName();
//        转译特殊字符
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(user.getName());
        if(exist)
            return Result.fail("用户名已被占用");
        userService.save(user);
        return Result.success();
    }

    @PostMapping("/forelogin")
    public Result login(@RequestBody User user, HttpSession session){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        User u = userService.findByNameAndPassword(name, user.getPassword());
        if(u==null)
            return Result.fail("账号或密码错误");
        session.setAttribute("user",u);
        return Result.success();
    }

//  返回商品详情信息
    @GetMapping("/foreProduct/{pid}")
    public Result findProduct(@PathVariable("pid")int pid){
        Product product = productService.findById(pid);
//        设置商品的单张和详情图
        List<ProductImage> singleTypeImages = productImageService.listSingleTypeImage(product);
        List<ProductImage> detailTypeImages = productImageService.listDetailTypeImage(product);
        product.setProductSingleImages(singleTypeImages);
        product.setProductDetailImages(detailTypeImages);
        productImageService.setFirstImage2Product(product);
        productService.setSaleAndReviewCount(product);
//        返回商品的评论信息,属性值信息和商品信息
        List<Review> reviews = reviewService.findByProduct(product);
        List<PropertyValue> propertyValues = propertyValueService.findByProductOrderByIdDesc(product);
        Map<String,Object> map = new HashMap<>();
        map.put("product",product);
        map.put("reviews",reviews);
        map.put("propertyValues",propertyValues);
        return Result.success(map);
    }

//    检查当前是否登录,用于模态登录
    @GetMapping("/foreCheckLogin")
    public Result checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user!=null)
            return Result.success();
        return Result.fail("未登录状态");
    }

//    根据排序规则显示分类下的商品,sort是排序规则
    @GetMapping("/foreCategory/{cid}")
    public Category productOfCategory(@PathVariable("cid")int cid,String sort){
//        先查出分类
        Category category = categoryService.findById(cid);
//        然后给分类对象填充商品列表数据
        productService.fill(category);
//        给每个商品设置销量和评价数量
        productService.setSaleAndReviewCount(category.getProducts());
//       将每个商品中的分类属性移除掉,防止递归
        categoryService.removeCategoryFromProduct(category);
        if(sort!=null){
            List<Product> products = category.getProducts();
            switch (sort){
                case "all":
                    Collections.sort(products,new ProductAllCompartor());
                    break;
                case "date":
                    Collections.sort(products,new ProductDateCompartor());
                    break;
                case "price":
                    Collections.sort(products,new ProductPriceCompartor());
                    break;
                case "review":
                    Collections.sort(products,new ProductReviewCompartor());
                case "sale":
                    Collections.sort(products,new ProductSaleCompartor());
            }
        }
        return category;
    }

    @GetMapping("/foreSearch")
    public List<Product> search(String keyword,@RequestParam(value = "start",defaultValue = "0")int start,
                                @RequestParam(value = "size",defaultValue = "20")int size){
        if(keyword==null){
            keyword = "";
        }
        List<Product> products = productService.findByKeyword(keyword, start, size);
        productImageService.setFirstImage2Products(products);
        productService.setSaleAndReviewCount(products);
        return products;
    }

    @GetMapping("/foreBuyOne")
    public Object buyone(int pid,int num,HttpSession session){
        return buyAndAddCart(pid,num,session);
    }

    public int buyAndAddCart(int pid, int num, HttpSession session){
//        先查出要购买的商品
        Product product = productService.findById(pid);
        int oiid = 0;
//      然后查找该用户购物车中已有订单项
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.findByUser(user);
        boolean found = false;
        for (OrderItem oi:orderItems){
//            若购物车中有相同商品订单项,则将该商品数量修改
            if(oi.getProduct().getId()==pid){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                oiid = oi.getId();
                found = true;
                break;
            }
        }
        if(!found){
//            若没有该商品对应的订单项,则新增订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItemService.save(orderItem);
            oiid = orderItem.getId();
        }
        return oiid;
    }

//    立即购买和结算
    @GetMapping("/foreBuy")
    public Result foreBuy(String[] oiid,HttpSession session){
//        立即购买有一个订单项,购物车中结算有多个订单项,所以统一用数组来接收
        List<OrderItem> orderItems = new ArrayList<>();
//        该订单商品总额
        int totalPrice = 0;
//        查出来每个订单项,算出总额并将订单项加入集合中
        for (String s : oiid){
            int id = Integer.parseInt(s);
            OrderItem orderItem = orderItemService.findById(id);
            totalPrice += orderItem.getNumber()*orderItem.getProduct().getPromotePrice();
            orderItems.add(orderItem);
        }
        session.setAttribute("orderItems",orderItems);
//        给订单商品设置图片
        productImageService.setFirstImage2OrderItems(orderItems);
        Map<String,Object> map = new HashMap<>();
        map.put("orderItems",orderItems);
        map.put("totalPrice",totalPrice);
        return Result.success(map);
    }

//    加入购物车
    @GetMapping("/foreAddCart")
    public Result foreAddCart(int pid,int num,HttpSession session){
        buyAndAddCart(pid, num, session);
        return Result.success();
    }

//    显示购物车内容
    @GetMapping("/foreCart")
    public List<OrderItem> foreCart(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.findByUser(user);
        productImageService.setFirstImage2OrderItems(orderItems);
        return orderItems;
    }

//    删除订单项
    @GetMapping("/foreDeleteOrderItem")
    public Result foreDeleteOrderItem(int oiid,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return Result.fail("未登录");
        }
        orderItemService.delete(oiid);
        return Result.success();
    }

//    更新订单项商品数量
    @GetMapping("/foreChangeOrderIetm")
    public Result foreChangeOrderIetm(int pid,int num,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return Result.fail("未登录状态");
        }
        Product product = new Product();
        product.setId(pid);
        OrderItem orderItem = orderItemService.findByUserAndAndProduct(user, product);
        orderItem.setNumber(num);
        orderItemService.update(orderItem);
        return Result.success();
    }

//    提交订单
    @PostMapping("/foreOrder")
    public Result foreOrder(@RequestBody Order order,HttpSession session){
        User user = (User) session.getAttribute("user");
//        生成订单号
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ RandomUtils.nextInt(11111);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
//        待付款状态
        order.setStatus(OrderService.WAITPAY);
        List<OrderItem> orderItems = (List<OrderItem>) session.getAttribute("orderItems");
        float totalPrice = orderService.save(order, orderItems);
        Map<String,Object> map = new HashMap<>();
        map.put("totalPrice",totalPrice);
        map.put("oid",order.getId());
        return Result.success(map);
    }

//    付款成功页显示数据
    @GetMapping("/forePayed")
    public Result forePayed(int oid){
        Order order = orderService.findById(oid);
        order.setStatus(OrderService.WAITDELIVERY);
        order.setPayDate(new Date());
        orderService.update(order);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR,7);
        Map<String,Object> map = new HashMap<>();
        map.put("order",order);
        map.put("date",calendar.getTime());
        return Result.success(map);
    }

//    订单页显示数据
    @GetMapping("/foreBought")
    public List<Order> foreBought(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderService.findNotDeleteFillOrderIetms(user);
        orderService.removeOrderFromOrderItem(orders);
        return orders;
    }

//    订单页删除订单
    @GetMapping("/foreDeleteOrder")
    public Result foreDeleteOrder(int oid){
        Order order = orderService.findById(oid);
//        修改订单状态为已删除状态
        order.setStatus(OrderService.DELETE);
        orderService.update(order);
        return Result.success();
    }

//    确认收货页显示数据
    @GetMapping("/foreConfirm")
    public Order foreConfirm(int oid){
        Order order = orderService.findById(oid);
//        填充订单项
        orderItemService.fill(order);
        orderService.removeOrderFromOrderItem(order);
//        计算订单总额
        orderService.caculateTotalPrice(order);
        return order;
    }

//    收货成功页面
    @GetMapping("/foreOrderConfirm")
    public Result foreOrderConfirm(int oid){
        Order order = orderService.findById(oid);
//        设置确认收货日期
        order.setConfirmDate(new Date());
//        修改订单状态为待评价状态
        order.setStatus(OrderService.WAITREVIEW);
        orderService.update(order);
        return Result.success();
    }

//    评论页显示数据
    @GetMapping("/foreReview")
    public Result foreReview(int oid){
        Order order = orderService.findById(oid);
//        给订单填充订单项
        orderItemService.fill(order);
        orderService.removeOrderFromOrderItem(order);
//        查找订单项第一个商品
        Product product = order.getOrderItems().get(0).getProduct();
//        设置商品的销量和评价数
        productService.setSaleAndReviewCount(product);
//        查询所有该商品下的评论
        List<Review> reviews = reviewService.findByProduct(product);
        Map<String,Object> map = new HashMap<>();
        map.put("order",order);
        map.put("p",product);
        map.put("reviews",reviews);
        return Result.success(map);
    }

//    提交评论
    @GetMapping("/foreReviewCommit")
    public Result foreReviewCommit(int pid,int oid,String content,HttpSession session){
        content = HtmlUtils.htmlEscape(content);
//        取评论的用户
        User user = (User) session.getAttribute("user");
//        修改订单状态为已完成
        Order order = orderService.findById(oid);
        order.setStatus(OrderService.FINISH);
//        添加评论
        Review review = new Review();
        review.setUser(user);
        review.setContent(content);
        review.setCreateDate(new Date());
        review.setProduct(productService.findById(pid));
        reviewService.save(review);
        return Result.success();
    }

}
