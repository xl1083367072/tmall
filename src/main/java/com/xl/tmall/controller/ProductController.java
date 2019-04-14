package com.xl.tmall.controller;

import com.xl.tmall.pojo.Product;
import com.xl.tmall.service.ProductImageService;
import com.xl.tmall.service.ProductService;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;

    @GetMapping("/categories/{cid}/products")
    public PageNavigator<Product> findAll(@PathVariable("cid")int cid, @RequestParam(value = "start",defaultValue = "0")int start,
                                          @RequestParam(value = "size",defaultValue = "10")int size){
        PageNavigator<Product> pageNavigator = productService.findAll(cid, start, size, 5);
        productImageService.setFirstImage2Products(pageNavigator.getContent());
        return pageNavigator;
    }

    @PostMapping("/products")
    public Product save(@RequestBody Product product){
        productService.save(product);
        return product;
    }

    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable("id") int id){
        productService.delete(id);
        return null;
    }

    @GetMapping("/products/{id}")
    public Product findById(@PathVariable("id") int id){
        Product product = productService.findById(id);
        return product;
    }

    @PutMapping("/products")
    public Product update(@RequestBody Product product){
        productService.update(product);
        return product;
    }

}
