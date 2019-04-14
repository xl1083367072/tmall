package com.xl.tmall.controller;

import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.PropertyValue;
import com.xl.tmall.service.ProductService;
import com.xl.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyValueController {

    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ProductService productService;

    @GetMapping("/products/{pid}/propertyValues")
    public List<PropertyValue> findAll(@PathVariable("pid")int pid){
        Product product = productService.findById(pid);
        propertyValueService.init(product);
        List<PropertyValue> propertyValues = propertyValueService.findByProductOrderByIdDesc(product);
        return propertyValues;
    }

    @PutMapping("/propertyValues")
    public PropertyValue update(@RequestBody PropertyValue propertyValue){
        propertyValueService.update(propertyValue);
        return propertyValue;
    }
}
