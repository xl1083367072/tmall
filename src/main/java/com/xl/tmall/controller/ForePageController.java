package com.xl.tmall.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ForePageController {

    @GetMapping("/")
    public String index(){
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home(){
        return "fore/home";
    }

    //    注册页面
    @GetMapping("/register")
    public String register(){
        return "fore/register";
    }

//    注册成功页面
    @GetMapping("/registerSuccess")
    public String registerSuccess(){
        return "fore/registerSuccess";
    }

//    登录页面
    @GetMapping("/login")
    public String login(){
        return "fore/login";
    }

//    登出
    @GetMapping("/forelogout")
    public String forelogout(HttpSession session){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            subject.logout();
        }
        return "fore/home";
    }

//    商品详情页
    @GetMapping("/product")
    public String product(){
        return "fore/product";
    }

//    分类商品页面
    @GetMapping("/category")
    public String category(){
        return "fore/category";
    }

//    搜索结果页
    @GetMapping("/search")
    public String search(){
        return "fore/search";
    }

//    结算页面
    @GetMapping("/buy")
    public String buy(){
        return "fore/buy";
    }

//    购物车页面
    @GetMapping("/cart")
    public String cart(){
        return "fore/cart";
    }

//    付款页面
    @GetMapping("/aliPay")
    public String pay(){
        return "fore/aliPay";
    }

//    付款成功页面
    @GetMapping("/payed")
    public String payed(){
        return "fore/payed";
    }

//    订单页面
    @GetMapping("/bought")
    public String bought(){
        return "fore/bought";
    }

//    确认收货页面
    @GetMapping("/confirm")
    public String confirmPay(){
        return "fore/confirm";
    }

//    收获成功页面
    @GetMapping("/orderConfirmed")
    public String orderConfirm(){
        return "fore/confirmed";
    }

//    评价页面
    @GetMapping("/review")
    public String review(){
        return "fore/review";
    }

}
