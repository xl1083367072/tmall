package com.xl.tmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

//    进入后台
    @GetMapping("/admin")
    public String admin(){
        return "redirect:adminCategoryList";
    }

//    分类列表页面
    @GetMapping("/adminCategoryList")
    public String adminCategoryList(){
        return "admin/listCategory";
    }
//   分类编辑页面
    @GetMapping("/adminCategoryEdit")
    public String adminCategoryEdit(){
        return "admin/editCategory";
    }

//    属性列表页面
    @GetMapping("/adminPropertyList")
    public String adminPropertyList(){
        return "admin/listProperty";
    }
//  属性编辑页面
    @GetMapping("/adminPropertyEdit")
    public String adminPropertyEdit(){
        return "admin/editProperty";
    }

//    商品列表页面
    @GetMapping("/adminProductList")
    public String adminProductList(){
        return "admin/listProduct";
    }
//     商品编辑页面
    @GetMapping("/adminProductEdit")
    public String adminProductEdit(){
        return "admin/editProduct";
    }

//    商品图片列表页面
    @GetMapping("/adminProductImageList")
    public String adminProductImageList(){
        return "admin/listProductImage";
    }

//    商品属性值编辑页面
    @GetMapping("/adminPropertyValueEdit")
    public String adminPropertyValueEdit(){
        return "admin/editPropertyValue";
    }

//    用户列表页面
    @GetMapping("/adminUserList")
    public String adminUserList(){
        return "admin/listUser";
    }

//    订单列表页面
    @GetMapping("/adminOrderList")
    public String adminOrderList(){
        return "admin/listOrder";
    }

}
