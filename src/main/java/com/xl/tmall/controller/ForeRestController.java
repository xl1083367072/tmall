package com.xl.tmall.controller;

import com.xl.tmall.compartor.*;
import com.xl.tmall.pojo.*;
import com.xl.tmall.service.*;
import com.xl.tmall.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        session.setAttribute("user",user);
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
    public int buyone(int pid,int num,HttpSession session){
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

}
